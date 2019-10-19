package com.virtuary.app.firebase

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val documentId: String? = null,
    var name: String? = null,
    var image: String? = null,
    var alias: Map<String, String>? = null,
    var item: List<Item>? = null
)
