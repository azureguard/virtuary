package com.virtuary.app.screens.login

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LoginViewModel : ViewModel() {
    val email = ObservableField("")
    val password = ObservableField("")
    private val inProgress = MutableLiveData<Boolean>(false)
    private val isSuccess = MutableLiveData<Boolean>(false)
    private val errorMessage = MutableLiveData<String>("")

    fun getInProgress(): LiveData<Boolean> {
        return inProgress
    }

    fun getIsSuccess(): LiveData<Boolean> {
        return isSuccess
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    private val auth = FirebaseAuth.getInstance()

    // Event which triggered when the email inputted is invalid
    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean>
        get() = _invalidEmail

    // Event which triggered when the password inputted is invalid
    private val _invalidPassword = MutableLiveData<Boolean>()
    val invalidPassword: LiveData<Boolean>
        get() = _invalidPassword

    fun onClick() {
        _invalidEmail.value =
            email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()!!).matches()
        _invalidPassword.value = password.get() == null || password.get()!!.isEmpty()
        if (!(invalidEmail.value!! || invalidPassword.value!!)) {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email.get()!!, password.get()!!).addOnCompleteListener {
                isSuccess.value = it.isSuccessful
                if (!it.isSuccessful) {
                    try {
                        throw it.exception!!
                    } catch (e: Exception) {
                        when (e) {
                            is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> errorMessage.value =
                                "Invalid email or password"
                            else -> errorMessage.value = "Unable to reach server"
                        }
                    }
                    inProgress.value = false
                }
            }
        }
    }
}
