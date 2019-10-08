package com.virtuary.app.screens.manageAccount

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChangePasswordViewModel : ViewModel() {
    val currentPassword = MutableLiveData("")
    val newPassword = MutableLiveData("")

    fun onClick() {

    }
}
