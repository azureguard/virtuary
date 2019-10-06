package com.virtuary.app.screens.item.addEditItem

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.toObject
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddEditItemViewModel(item: Item?) : ViewModel() {
    private val _item: Item? = item

    val title = MutableLiveData(item?.name ?: "")
    val location = MutableLiveData(item?.currentLocation ?: "")
    val story = MutableLiveData(item?.story ?: "")
    val image = MutableLiveData<Bitmap>()
    private val repository: FirestoreRepository = FirestoreRepository()
    private val storageRepository: StorageRepository by lazy { StorageRepository() }

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

    init {
        _addedRelatedTo.value = item?.relations?.toMutableList() ?: mutableListOf()
        val allUsers = listOf("-", "None", "Daryl", "Michelle", "Maurice", "SK")
        _selectionRelatedTo.value =
            allUsers.filterNot { _addedRelatedTo.value?.contains(it) ?: false }.toMutableList()
        _isEdit.value = item != null
    }

    // check title input if it is empty
    private val _emptyTitle = MutableLiveData<Boolean>()
    val emptyTitle: LiveData<Boolean>
        get() = _emptyTitle

    fun onClick() {
        _emptyTitle.value = title.value?.isEmpty() ?: true
        if (!emptyTitle.value!!) {
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
                    } catch (e: FirebaseException) {
                        _isError.value = true
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
                        withContext(Dispatchers.IO) { repository.editItem(_item) }
                        _document.value = _item
                    } catch (e: FirebaseException) {
                        _isError.value = true
                    }
                }
            }
            _inProgress.value = false
        }
    }

    private suspend fun uploadImage(): String? {
        return if (image.value != null) {
            withContext(Dispatchers.IO) {
                storageRepository.uploadImage(image.value!!).await().storage.toString()
            }
        } else {
            null
        }
    }

    // related to item selected
    fun onItemSelected(itemIndex: Int): Boolean {
        if (_selectionRelatedTo.value!![itemIndex] != "None" && _selectionRelatedTo.value!![itemIndex] != "-") {
            addRelatedTo(_selectionRelatedTo.value!![itemIndex])
            _selectionRelatedTo.value!!.removeAt(itemIndex)

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
