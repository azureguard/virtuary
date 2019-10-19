package com.virtuary.app.screens.signUp

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.virtuary.app.R
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.User
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    val name = ObservableField("")
    val email = ObservableField("")
    val password = ObservableField("")
    private val inProgress = MutableLiveData<Boolean>(false)
    private val isSuccess = MutableLiveData<Boolean>(false)
    private val errorMessage = MutableLiveData<Int>()
    private val fbAuth = FirebaseAuth.getInstance()
    private val repository: FirestoreRepository = FirestoreRepository()
    private val currUser = User()

    fun getInProgress(): LiveData<Boolean> {
        return inProgress
    }

    fun getIsSuccess(): LiveData<Boolean> {
        return isSuccess
    }

    fun getErrorMessage(): LiveData<Int> {
        return errorMessage
    }

    // Event which triggered when the email inputted is invalid
    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean>
        get() = _invalidEmail

    // Event which triggered when the password inputted is invalid
    private val _invalidPassword = MutableLiveData<Boolean>()
    val invalidPassword: LiveData<Boolean>
        get() = _invalidPassword

    // Event which triggered when the name inputted is invalid
    private val _invalidName = MutableLiveData<Boolean>()
    val invalidName: LiveData<Boolean>
        get() = _invalidName

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun onClick() {
        _invalidEmail.value =
            email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get() as CharSequence).matches()
        _invalidPassword.value =
            password.get() == null || password.get()!!.isEmpty() || password.get()!!.length < 6
        _invalidName.value = name.get() == null || name.get()!!.isEmpty()

        if (!(invalidEmail.value!! || invalidPassword.value!! || invalidName.value!!)) {
            inProgress.value = true
            fbAuth.createUserWithEmailAndPassword(email.get()!!, password.get()!!)
                .addOnCompleteListener { task ->
                    isSuccess.value = task.isSuccessful
                    if (task.isSuccessful) {
                        val user = fbAuth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name.get())
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener {
                                if (!isSuccess.value!!) {
                                    errorMessage.value = R.string.error_user_profile_not_set
                                }
                            }

                        // Update user live data to be observed and passed to main activity
                        _userName.value = name.get()


                        currUser.name = name.get()
                        if (user != null) {
                            viewModelScope.launch {
                                repository.addUser(currUser, user.uid)
                            }
                        }
                    } else {
                        errorMessage.value = R.string.error_server_unreachable
                    }
                    inProgress.value = false
                }
        }
    }
}
