package com.virtuary.app.screens.item.addEditItem

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.search.client.ClientSearch
import com.algolia.search.client.Index
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.virtuary.app.firebase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddEditItemViewModel(item: Item?, userDB: HashMap<String, User>) : ViewModel() {
    companion object {
        const val API_KEY = "api_key"
        const val APP_ID = "application_id"
        const val INDEX_NAME = "index_name"
    }

    private val _item: Item? = item

    val title = MutableLiveData(item?.name ?: "")
    val location = MutableLiveData(item?.currentLocation ?: "")
    val story = MutableLiveData(item?.story ?: "")
    val image = MutableLiveData<Bitmap>()
    private val repository: FirestoreRepository = FirestoreRepository()
    private val storageRepository: StorageRepository by lazy { StorageRepository() }

    private lateinit var algoliaConfig: Map<String, Any>

    private val _selectionRelatedTo = MutableLiveData<MutableList<String>>()
    val selectionRelatedTo: LiveData<MutableList<String>>
        get() = _selectionRelatedTo

    private val _addedRelatedTo = MutableLiveData<MutableList<String>>()
    val addedRelatedTo: LiveData<MutableList<String>>
        get() = _addedRelatedTo

    private val _inProgress = MutableLiveData<Boolean>(false)
    val inProgress: LiveData<Boolean>
        get() = _inProgress

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean>
        get() = _isError

    private val _isEdit = MutableLiveData<Boolean>(false)
    val isEdit: LiveData<Boolean>
        get() = _isEdit

    private val _document = MutableLiveData<Item>()
    val document: LiveData<Item>
        get() = _document

    private val _itemImage = MutableLiveData<StorageReference>()
    val itemImage: LiveData<StorageReference>
        get() = _itemImage

    init {
        _addedRelatedTo.value = item?.relations?.toMutableList() ?: mutableListOf()
        val allUsers = mutableListOf("Please select here")

        for (userId in userDB.keys) {
            allUsers.add(userId)
        }

        _selectionRelatedTo.value =
            allUsers.filterNot { _addedRelatedTo.value?.contains(it) ?: false }.toMutableList()
        _isEdit.value = item != null
        _itemImage.value = storageRepository.getImage(item?.image)
        viewModelScope.launch {
            algoliaConfig = repository.getAlgoliaConfig()
        }
    }

    // check title input if it is empty
    private val _emptyTitle = MutableLiveData<Boolean>()
    val emptyTitle: LiveData<Boolean>
        get() = _emptyTitle

    fun onClick() {
        _emptyTitle.value = title.value?.isEmpty() ?: true
        if (!emptyTitle.value!!) {
            val appID = ApplicationID(algoliaConfig[APP_ID] as String)
            val apiKey = APIKey(algoliaConfig[API_KEY] as String)
            val client = ClientSearch(appID, apiKey)
            val indexName = IndexName(algoliaConfig[INDEX_NAME] as String)
            val index = client.initIndex(indexName)
            _isError.value = false
            _inProgress.value = true
            if (_item == null) {
                viewModelScope.launch {
                    val item = Item(
                        name = title.value,
                        currentLocation = location.value,
                        story = story.value,
                        relations = addedRelatedTo.value,
                        image = uploadImage()
                    )

                    try {
                        _document.value = withContext(Dispatchers.IO) {
                            repository.addItem(item).await().get().await().toObject<Item>()
                        }
                        addToIndex(index)
                    } catch (e: FirebaseException) {
                        _isError.value = true
                        _inProgress.value = false
                    }
                }
            } else {
                _item.name = title.value
                _item.currentLocation = location.value
                _item.story = story.value
                _item.relations = addedRelatedTo.value
                viewModelScope.launch {
                    _item.image = uploadImage()
                    try {
                        withContext(Dispatchers.IO) {
                            repository.editItem(_item)
                        }
                        _document.value = _item
                        addToIndex(index)
                    } catch (e: FirebaseException) {
                        _isError.value = true
                        _inProgress.value = false
                    }
                }
            }
        }
    }

    private suspend fun addToIndex(index: Index) {
        val serializableItem = ItemSerializable(
            documentId = _document.value!!.documentId,
            name = _document.value!!.name,
            originalLocation = _document.value!!.originalLocation,
            currentLocation = _document.value!!.currentLocation,
            story = _document.value!!.story,
            relations = _document.value!!.relations,
            image = _document.value!!.image
        )
        withContext(Dispatchers.IO) {
            index.saveObject(
                ItemSerializable.serializer(),
                serializableItem
            )
        }
    }

    private suspend fun uploadImage(): String? {
        return if (image.value != null) {
            withContext(Dispatchers.IO) {
                storageRepository.uploadImage(image.value!!).await().storage.toString()
            }
        } else {
            _item?.image
        }
    }

    // related to item selected
    fun onItemSelected(itemIndex: Int): Boolean {
        if (_selectionRelatedTo.value!![itemIndex] != "Please select here") {
            addRelatedTo(_selectionRelatedTo.value!![itemIndex])
            _selectionRelatedTo.value!!.removeAt(itemIndex)
            val tempSelectionRelatedTo = mutableListOf<String>()
            tempSelectionRelatedTo.addAll(_selectionRelatedTo.value!!)
            _selectionRelatedTo.value = tempSelectionRelatedTo

            return true
        }

        return false
    }

    // remove click on chips
    fun onRemoveClick(name: String) {
        _selectionRelatedTo.add(name)
        _addedRelatedTo.value!!.remove(name)
    }

    private fun addRelatedTo(item: String) {
        _addedRelatedTo.add(item)
    }

}

// extension function
fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
    val updatedItems = this.value as ArrayList
    updatedItems.add(item)
    this.value = updatedItems
}
