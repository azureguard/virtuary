package com.virtuary.app.screens.item.editItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class EditItemViewModelFactory(private val title: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditItemViewModel::class.java)) {
            return EditItemViewModel(title) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
