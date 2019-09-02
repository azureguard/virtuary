package com.virtuary.app.screens.signUp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField

class SignUpViewModel : ViewModel() {
    val name = ObservableField("")
    val email = ObservableField("")
    val password = ObservableField("")

    fun onClick() {
        Log.i("Testing OnClick Login", "${email.get()} and ${password.get()} and ${name.get()}");
    }
}