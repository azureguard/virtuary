package com.virtuary.app.screens.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    private val _relatedTo = MutableLiveData<MutableList<String>>()
    val relatedTo: LiveData<MutableList<String>>
        get() = _relatedTo

    init {
        _relatedTo.value = mutableListOf()
    }

    fun addRelatedTo(relations : List<String>?) {
        _relatedTo.value = relations?.toMutableList() ?: mutableListOf()
    }
}
