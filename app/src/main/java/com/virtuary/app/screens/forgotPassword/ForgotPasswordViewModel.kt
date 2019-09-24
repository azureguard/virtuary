package com.virtuary.app.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    val email = ObservableField("")
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val isSuccess = MutableLiveData<Boolean>()
    fun getIsSuccess(): LiveData<Boolean> {
        return isSuccess
    }

    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean>
        get() = _invalidEmail

    fun onClick() {
        _invalidEmail.value =
            email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()!! as CharSequence).matches()
        if (!invalidEmail.value!!) {
            auth.sendPasswordResetEmail(email.get()!!)
                .addOnCompleteListener { task -> isSuccess.value = task.isSuccessful }
        }
    }
}
