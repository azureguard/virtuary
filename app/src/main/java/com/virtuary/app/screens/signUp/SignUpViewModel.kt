package com.virtuary.app.screens.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignUpViewModel : ViewModel() {
    val name = ObservableField("")
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

    // Event which triggered when the email inputted is invalid
    private val _invalidName = MutableLiveData<Boolean>()
    val invalidName: LiveData<Boolean>
        get() = _invalidName

    fun onClick() {
        _invalidEmail.value = email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()).matches()
        _invalidPassword.value = password.get() == null || password.get()!!.isEmpty()
        _invalidName.value = name.get() == null || name.get()!!.isEmpty()
        Log.i("Testing OnClick Login", "${email.get()} and ${password.get()} and ${name.get()}")
    }
}