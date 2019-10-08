package com.virtuary.app.screens.manageAccount

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.virtuary.app.firebase.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditProfileDetailsViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val storageRepository = StorageRepository()
    private val user = auth.currentUser

    val name = MutableLiveData(user?.displayName ?: "")
    val email = MutableLiveData(user?.email ?: "")
    val image = MutableLiveData(storageRepository.getImage(user?.photoUrl as String?))

    fun updateName(value: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(value)
            .build()
        viewModelScope.launch {
            if (withContext(Dispatchers.IO) {
                    try {
                        user?.updateProfile(profileUpdates)?.await()
                        return@withContext true
                    } catch (e: FirebaseAuthInvalidUserException) {
                        return@withContext false
                    }
                }) {
                name.value = value
            } else {
                TODO()
            }

        }
    }

    fun updateImage(value: Bitmap) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(""))
            .build()
        user?.updateProfile(profileUpdates)
    }

    fun updateEmail(value: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                user?.updateEmail(value)?.await()
            } catch (e: Exception) {
                when (e) {
                    is FirebaseAuthUserCollisionException -> TODO()
                    is FirebaseAuthRecentLoginRequiredException -> TODO()
                }
            }
        }
    }
}
