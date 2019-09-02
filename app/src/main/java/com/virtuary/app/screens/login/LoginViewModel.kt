package com.virtuary.app.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField

class LoginViewModel : ViewModel() {
    val email = ObservableField("")
    val password = ObservableField("")

    fun onClick() {
        Log.i("Testing OnClick Login", "${email.get()} and ${password.get()}");
    }
}