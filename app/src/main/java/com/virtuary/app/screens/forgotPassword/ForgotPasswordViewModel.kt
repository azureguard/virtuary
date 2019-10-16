package com.virtuary.app.screens.forgotPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val email = MutableLiveData("")

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean> = _invalidEmail

    private val _inProgress = MutableLiveData<Boolean>()
    val inProgress: LiveData<Boolean> = _inProgress

    fun onClick() {
        _invalidEmail.value =
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.value as CharSequence).matches()
        if (!invalidEmail.value!!) {
            _inProgress.value = true
            auth.sendPasswordResetEmail(email.value!!)
                .addOnCompleteListener { task ->
                    _isSuccess.value = task.isSuccessful
                    _inProgress.value = false
                }
        }
    }
}
