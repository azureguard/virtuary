package com.virtuary.app.screens.login

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel : ViewModel() {
    val email = ObservableField("")
    val password = ObservableField("")

    // Event which triggered when the email inputted is invalid
    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean>
        get() = _invalidEmail

    // Event which triggered when the email inputted is invalid
    private val _invalidPassword = MutableLiveData<Boolean>()
    val invalidPassword: LiveData<Boolean>
        get() = _invalidPassword

    fun onClick() {
        _invalidEmail.value = email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()).matches()
        _invalidPassword.value = password.get() == null || password.get()!!.isEmpty()
    }
}