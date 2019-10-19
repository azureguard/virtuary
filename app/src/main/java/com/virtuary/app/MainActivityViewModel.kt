package com.virtuary.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.virtuary.app.firebase.FirestoreRepository
import com.virtuary.app.firebase.User

class MainActivityViewModel : ViewModel() {
    val name = MutableLiveData("")
    val imageUploaded = MutableLiveData<String>()
    var userDB = HashMap<String, User>()
    private val repository: FirestoreRepository = FirestoreRepository()
    private var query: Query = repository.queryAllUsers()

    init {
        query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                return@addSnapshotListener
            }

            val users = querySnapshot?.toObjects(User::class.java)
            if (users != null) {
                for (user in users) {
                    userDB[user.documentId!!] = user
                }
            }
        }
    }
}
