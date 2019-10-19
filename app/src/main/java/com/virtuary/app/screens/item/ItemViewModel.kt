package com.virtuary.app.screens.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.search.model.ObjectID
import com.google.firebase.storage.StorageReference
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.util.AlgoliaClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ItemViewModel(item: Item) : ViewModel() {
    private val repository = FirestoreRepository()
    private val storageRepository = StorageRepository()
    private val _relatedTo = MutableLiveData<MutableList<String>>()
    val relatedTo: LiveData<MutableList<String>> = _relatedTo

    private val _itemImage = MutableLiveData<StorageReference>()
    val itemImage: LiveData<StorageReference> = _itemImage

    private val _deleteTriggered = MutableLiveData(false)
    val deleting: LiveData<Boolean> = _deleteTriggered

    private val _deleteDone = MutableLiveData(false)
    val deleteDone: LiveData<Boolean> = _deleteDone

    init {
        _relatedTo.value = mutableListOf()
        _itemImage.value = storageRepository.getImage(item.image)
    }

    fun addRelatedTo(relations: List<String>?) {
        _relatedTo.value = relations?.toMutableList() ?: mutableListOf()
    }

    fun deleteItem(item: Item) {
        _deleteTriggered.value = true
        viewModelScope.launch() {
            val index = AlgoliaClient.getIndex("item")
            index.deleteObject(ObjectID(item.documentId!!))
            repository.deleteItem(item).await()
            _deleteDone.value = true
        }
    }
}
