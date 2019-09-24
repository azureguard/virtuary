package com.virtuary.app.screens.family.member.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item

class MemberItemViewModel : ViewModel() {
    private val _artifacts = MutableLiveData<MutableList<Item>>()
    val artifacts: LiveData<MutableList<Item>>
        get() = _artifacts

    private val repository: FirestoreRepository = FirestoreRepository()

    private fun getAllItems(): MutableList<Item>? {
        repository.getAllItems().addOnSuccessListener {
            _artifacts.value = it.toObjects(Item::class.java)
        }
        return _artifacts.value
    }

    init {
        _artifacts.value = getAllItems()
    }
}
