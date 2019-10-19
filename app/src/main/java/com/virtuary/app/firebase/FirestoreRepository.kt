package com.virtuary.app.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val firestoreDB = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    fun queryAllUsers(): Query {
        return firestoreDB.collection("User").orderBy("name")
    }

    // add user to firebase
    fun addUser(user: User, userId: String): Task<Void> {
        return firestoreDB.collection("User").document(userId).set(user)
    }

    // update the user image in firebase
    fun updateUserImage(userId: String, userImage: String): Task<Void> {
        return firestoreDB.collection("User").document(userId).update("image", userImage)
    }

    // update the user name in firebase
    fun updateUserName(userId: String, userName: String): Task<Void> {
        return firestoreDB.collection("User").document(userId).update("name", userName)
    }

    // update the user alias in firebase
    fun updateUserAlias(userId: String, currUserId: String, alias: String): Task<Void>{
        return firestoreDB.collection("User").document(userId).update(mapOf(
            "alias.${currUserId}" to alias
        ))
    }

    // add item to firebase
    fun addItem(item: Item): Task<DocumentReference> {
        return firestoreDB.collection("Item").add(item)
    }

    // edit item in firebase
    fun editItem(item: Item): Task<Void> {
        return firestoreDB.collection("Item").document(item.documentId!!).set(item)
    }

    // get all items from firebase
    fun getAllItems(): Task<QuerySnapshot> {
        return firestoreDB.collection("Item").get()
    }

    fun queryAllItems(): Query {
        return firestoreDB.collection("Item").orderBy("timestamp", Query.Direction.DESCENDING)
    }

    // delete specific item
    fun deleteItem(item: Item): Task<Void> {
        return firestoreDB.collection("Item").document(item.documentId!!).delete()
    }

    suspend fun getAlgoliaConfig(): Map<String, Any> {
        return firestoreDB.collection("config").document("algolia").get().await().data!!
    }

}
