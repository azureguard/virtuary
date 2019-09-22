package com.virtuary.app.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository {

    val TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser


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

    // delete specific item
    fun deleteItem(item: Item): Task<Void> {
        return firestoreDB.collection("Item").document(item.documentId!!).delete()
    }

}
