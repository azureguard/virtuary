package com.virtuary.app.screens.item.editItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.virtuary.app.firebase.Item
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class EditItemViewModelFactory(private val item: Item) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditItemViewModel::class.java)) {
            return EditItemViewModel(item) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
