package com.virtuary.app.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

class FirestoreViewModel : ViewModel() {

    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FirestoreRepository()
    var artifacts: MutableLiveData<List<Item>> = MutableLiveData()

    fun getAllItems(): LiveData<List<Item>> {
        firebaseRepository.getAllItems().addOnSuccessListener {
            val artifactsList : MutableList<Item> = mutableListOf()
            for (doc in it!!) {
                val artifact = doc.toObject(Item::class.java)
                artifactsList.add(artifact)
            }
            artifacts.value = artifactsList
        }
        return artifacts
    }
}
