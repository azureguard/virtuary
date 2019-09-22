package com.virtuary.app.firebase

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Item(
    @DocumentId
    val documentId: String? = null,
    val name: String? = null,
    val originalLocation: String? = null,
    val currentLocation: String? = null,
    val whenExist: Date? = null,
    val story: String? = null,
    val relations: List<String>? = null,
    val pictures: List<String>? = null
)
