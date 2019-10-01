package com.virtuary.app.screens.item.editItem

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.Item

class EditItemViewModel(item: Item) : ViewModel() {
    // ONLY "title" passed in from item view fragment
    private val _item: Item = item

    val title = ObservableField(item.name)
    val location = ObservableField(item.currentLocation)
    val story = ObservableField(item.story)
    private val repository: FirestoreRepository = FirestoreRepository()

    private val _selectionRelatedTo = MutableLiveData<MutableList<String>>()
    val selectionRelatedTo: LiveData<MutableList<String>>
        get() = _selectionRelatedTo

    private val _addedRelatedTo = MutableLiveData<MutableList<String>>()
    val addedRelatedTo: LiveData<MutableList<String>>
        get() = _addedRelatedTo

    init {
        _addedRelatedTo.value = item.relations as MutableList<String>?
        val allUsers = listOf("-", "None", "Daryl", "Michelle", "Maurice", "SK")
        val selected = item.relations
        _selectionRelatedTo.value =
            allUsers.filterNot { selected?.contains(it)!! } as MutableList<String>
    }

    // check title input if it is empty
    private val _emptyTitle = MutableLiveData<Boolean>()
    val emptyTitle: LiveData<Boolean>
        get() = _emptyTitle

    fun onClick() {
        _emptyTitle.value = title.get() == null || title.get()!!.isEmpty()
        if (!emptyTitle.value!!) {
            _item.name = title.get()
            _item.currentLocation = location.get()
            _item.story = story.get()
            _item.relations = addedRelatedTo.value
            repository.editItem(_item)
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

    private fun addSelectionList(selected: List<String>, unselected: List<String>) {


    }
}

// extension function
fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
    val updatedItems = this.value as ArrayList
    updatedItems.add(item)
    this.value = updatedItems
}
