package com.virtuary.app.screens.forgotPassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField

class ForgotPasswordViewModel : ViewModel() {
    val email = ObservableField("")

    fun onClick() {
        Log.i("Testing OnClick Login", "${email.get()}");
    }
}