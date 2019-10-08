package com.virtuary.app.screens.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.StorageReference
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository

class ItemViewModel(item: Item) : ViewModel() {
    private val repository = FirestoreRepository()
    private val storageRepository = StorageRepository()
    private val _relatedTo = MutableLiveData<MutableList<String>>()
    val relatedTo: LiveData<MutableList<String>>
        get() = _relatedTo

    private val _itemImage = MutableLiveData<StorageReference>()
    val itemImage: LiveData<StorageReference>
        get() = _itemImage

    init {
        _relatedTo.value = mutableListOf()
        _itemImage.value = storageRepository.getImage(item.image)
    }

    fun addRelatedTo(relations: List<String>?) {
        _relatedTo.value = relations?.toMutableList() ?: mutableListOf()
    }

    fun deleteItem(item: Item) {
        repository.deleteItem(item)
    }
}
