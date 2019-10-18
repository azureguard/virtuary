package com.virtuary.app.screens.manageAccount

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.storage.StorageReference
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditProfileDetailsViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val storageRepository = StorageRepository()
    private val user = auth.currentUser
    private val repository: FirestoreRepository = FirestoreRepository()

    val name = MutableLiveData(user?.displayName ?: "")
    val email = MutableLiveData(user?.email ?: "")
    val image = MutableLiveData<Bitmap>()

    private val _path = MutableLiveData<String>("")
    val path: LiveData<String> = _path

    private val _profileImage = MutableLiveData<StorageReference>()
    val profileImage: LiveData<StorageReference> = _profileImage

    private val _uploading = MutableLiveData<Boolean>()
    val uploading: LiveData<Boolean> = _uploading

    private val _authRequired = MutableLiveData<Boolean>()
    val authRequired: LiveData<Boolean> = _authRequired

    private val _authError = MutableLiveData<Boolean>()
    val authError: LiveData<Boolean> = _authError

    private val _lastEmail = MutableLiveData<String>()

    init {
        _profileImage.value = storageRepository.getImage(user?.photoUrl.toString())
    }

    fun updateName(value: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(value)
            .build()
        viewModelScope.launch {
            if (withContext(Dispatchers.IO) {
                    try {
                        user?.updateProfile(profileUpdates)?.await()
                        repository.updateUserName(user!!.uid, value).await()
                        return@withContext true
                    } catch (e: Exception) {
                        return@withContext false
                    }
                }) {
                name.value = value
            } else {
                TODO()
            }
        }
    }

    fun updateImage() {
        viewModelScope.launch {
            _uploading.value = true
            _path.value = if (image.value != null) {
                withContext(Dispatchers.IO) {
                    storageRepository.uploadProfileImage(image.value!!).await().storage.toString()
                }
            } else {
                ""
            }
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(_path.value))
                .build()
            user?.updateProfile(profileUpdates)

            // update the user image to the firebase
            if (user != null) {
                try {
                    withContext(Dispatchers.IO) {
                        repository.updateUserImage(user.uid, path.value!!)
                    }
                } catch (e: FirebaseException) {
                    // do nothing
                }
            }

            _uploading.value = false
        }
    }

    fun updateEmail(value: String, callback: ((result: Boolean) -> Unit)?) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            callback?.invoke(false)
        } else {
            viewModelScope.launch {
                if (
                    try {
                        user?.updateEmail(value)?.await()
                        true
                    } catch (e: Exception) {
                        when (e) {
                            is FirebaseAuthUserCollisionException -> _authError.value = true
                            is FirebaseAuthRecentLoginRequiredException -> {
                                _authRequired.value = true
                                _lastEmail.value = value
                            }
                        }
                        false
                    }
                ) {
                    email.value = value
                }
                callback?.invoke(true)
            }
        }
    }

    fun verifyPassword(
        currentPassword: String,
        callback: (result: Boolean) -> Unit
    ) {
        if (currentPassword.isEmpty()) {
            callback(false)
        }
        val credential = EmailAuthProvider.getCredential(user?.email ?: "", currentPassword)
        viewModelScope.launch {
            try {
                user?.reauthenticate(credential)?.await()
                callback(true)
                updateEmail(_lastEmail.value!!, null)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }
}
