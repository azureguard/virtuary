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
        addRelatedTo()
    }

    private fun addRelatedTo() {
        _relatedTo.value!!.add("Daryl")
        _relatedTo.value!!.add("Michelle")
        _relatedTo.value!!.add("Maurice")
        _relatedTo.value!!.add("xDDDDDDDD")
        _relatedTo.value!!.add("Steven")
        _relatedTo.value!!.add("Stephanie")
        _relatedTo.value!!.add("Kevin")
        _relatedTo.value!!.add("Joseph")
        _relatedTo.value!!.add("Peter")
    }
}
