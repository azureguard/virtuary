package com.virtuary.app.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    val email = ObservableField("")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess


    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean>
        get() = _invalidEmail


    private val _inProgress = MutableLiveData<Boolean>()
    val inProgress: LiveData<Boolean>
        get() = _inProgress


    fun onClick() {
        _invalidEmail.value =
            email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()!! as CharSequence).matches()
        if (!invalidEmail.value!!) {
            _inProgress.value = true
            auth.sendPasswordResetEmail(email.get()!!)
                .addOnCompleteListener { task ->
                    _isSuccess.value = task.isSuccessful
                    _inProgress.value = false
                }
        }
    }
}
