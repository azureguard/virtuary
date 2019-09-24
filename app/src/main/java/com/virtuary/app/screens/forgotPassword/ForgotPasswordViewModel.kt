package com.virtuary.app.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    val email = ObservableField("")
    private val isSuccess = MutableLiveData<Boolean>()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getIsSuccess(): LiveData<Boolean> {
        return isSuccess
    }

    fun onClick() {
        auth.sendPasswordResetEmail(email.get()!!)
            .addOnCompleteListener { task -> isSuccess.value = task.isSuccessful }
    }
}
