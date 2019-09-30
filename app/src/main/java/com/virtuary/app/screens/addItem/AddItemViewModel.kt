package com.virtuary.app.screens.addItem

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item

class AddItemViewModel : ViewModel() {
    val title = ObservableField("")
    val location = ObservableField("")
    val story = ObservableField("")
    private val repository: FirestoreRepository = FirestoreRepository()

    private val _selectionRelatedTo = MutableLiveData<MutableList<String>>()
    val selectionRelatedTo: LiveData<MutableList<String>>
        get() = _selectionRelatedTo

    private val _addedRelatedTo = MutableLiveData<MutableList<String>>()
    val addedRelatedTo: LiveData<MutableList<String>>
        get() = _addedRelatedTo

    init {
        _selectionRelatedTo.value = mutableListOf()
        _addedRelatedTo.value = mutableListOf()
        addSelectionList()
    }

    // check title input if it is empty
    private val _emptyTitle = MutableLiveData<Boolean>()
    val emptyTitle: LiveData<Boolean>
        get() = _emptyTitle

    private val _document = MutableLiveData<DocumentSnapshot>()
    val document: LiveData<DocumentSnapshot>
        get() = _document

    fun onClick() {
        _emptyTitle.value = title.get() == null || title.get()!!.isEmpty()
        if (!emptyTitle.value!!) {
            val item = Item(
                name = title.get(),
                currentLocation = location.get(),
                story = story.get(),
                relations = addedRelatedTo.value
            )

            repository.addItem(item).continueWith { it.result?.get() }
                .addOnSuccessListener { snapshotTask ->
                    snapshotTask?.continueWith { docSnapshot ->
                        _document.value = docSnapshot.result
                    }
                }
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

    private fun addSelectionList() {
        _selectionRelatedTo.value!!.add("-")
        _selectionRelatedTo.value!!.add("None")
        _selectionRelatedTo.value!!.add("Daryl")
        _selectionRelatedTo.value!!.add("michelle")
        _selectionRelatedTo.value!!.add("maurice")
        _selectionRelatedTo.value!!.add("sk")
    }
}

// extension function
fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
    val updatedItems = this.value as ArrayList
    updatedItems.add(item)
    this.value = updatedItems
}
