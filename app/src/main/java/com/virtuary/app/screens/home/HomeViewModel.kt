package com.virtuary.app.screens.home

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.virtuary.app.firebase.FirestoreRepository

class HomeViewModel : ViewModel() {
    private val repository: FirestoreRepository = FirestoreRepository()
    val query: Query = repository.queryAllItems()
}
