package com.virtuary.app.screens.item.addEditItem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.virtuary.app.firebase.Item
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class AddEditItemViewModelFactory(private val item: Item?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditItemViewModel::class.java)) {
            return AddEditItemViewModel(item) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
