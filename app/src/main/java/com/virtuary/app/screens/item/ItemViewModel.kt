package com.virtuary.app.screens.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item

class ItemViewModel : ViewModel() {
    private val repository: FirestoreRepository = FirestoreRepository()
    private val _relatedTo = MutableLiveData<MutableList<String>>()
    val relatedTo: LiveData<MutableList<String>>
        get() = _relatedTo

    init {
        _relatedTo.value = mutableListOf()
    }

    fun addRelatedTo(relations: List<String>?) {
        _relatedTo.value = relations?.toMutableList() ?: mutableListOf()
    }

    fun deleteItem(item: Item) {
        repository.deleteItem(item)
    }
}
