package com.virtuary.app.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item

class HomeViewModel : ViewModel() {

    private val _artifacts = MutableLiveData<MutableList<Item>>()
    val artifacts: LiveData<MutableList<Item>>
        get() = _artifacts

    private val repository: FirestoreRepository = FirestoreRepository()

    private fun getAllItems(): MutableList<Item>? {
        repository.getAllItems().addOnSuccessListener {
            val artifactsList: MutableList<Item> = mutableListOf()
            for (doc in it!!) {
                val artifact = doc.toObject(Item::class.java)
                artifactsList.add(artifact)
            }
            _artifacts.value = artifactsList
        }
        return _artifacts.value
    }

    init {
        _artifacts.value = getAllItems()
    }
}
