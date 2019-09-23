package com.virtuary.app.screens.signUp

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignUpViewModel : ViewModel() {
    val name = ObservableField("")
    val email = ObservableField("")
    val password = ObservableField("")

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

    fun onClick() {
        _invalidEmail.value = email.get() == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.get()).matches()
        _invalidPassword.value = password.get() == null || password.get()!!.isEmpty() || password.get()!!.length < 6
        _invalidName.value = name.get() == null || name.get()!!.isEmpty()
        val fbAuth = FirebaseAuth.getInstance()
        fbAuth.createUserWithEmailAndPassword(email.get()!!, password.get()!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = fbAuth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name.get())
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // TODO: handle case where sign up is successful but name is not?
                            }
                        }
                } else {
                    // TODO: Unable to make a toast without context
//                    Toast.makeText(
//                        baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
    }
}
