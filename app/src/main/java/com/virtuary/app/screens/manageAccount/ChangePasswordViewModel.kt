package com.virtuary.app.screens.manageAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChangePasswordViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser

    private val _invalidNewPassword = MutableLiveData<Boolean>()
    val invalidNewPassword: LiveData<Boolean> = _invalidNewPassword

    private val _invalidCurrentPassword = MutableLiveData<Boolean>()
    val invalidCurrentPassword = _invalidCurrentPassword

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess = _isSuccess

    private val _inProgress = MutableLiveData<Boolean>()
    val inProgress = _inProgress

    val currentPassword = MutableLiveData("")
    val newPassword = MutableLiveData("")

    fun onClick() {
        _invalidNewPassword.value = (newPassword.value?.length ?: 0) < 6
        if (!_invalidNewPassword.value!!) {
            _inProgress.value = true
            verifyPassword { isVerified ->
                _invalidCurrentPassword.value = !isVerified
                if (isVerified) {
                    viewModelScope.launch() {
                        try {
                            user?.updatePassword(newPassword.value!!)
                            _isSuccess.value = true
                        } catch (e: Exception) {
                            _isSuccess.value = false
                        }
                    }
                }
                _inProgress.value = false
            }
        }
    }

    private fun verifyPassword(callback: (result: Boolean) -> Unit) {
        if (currentPassword.value?.isNotEmpty()!!) {
            val credential =
                EmailAuthProvider.getCredential(user?.email ?: "", currentPassword.value!!)
            viewModelScope.launch {
                try {
                    user?.reauthenticate(credential)?.await()
                    callback(true)
                } catch (e: Exception) {
                    callback(false)
                }
            }
        } else {
            callback(false)
        }
    }
}
