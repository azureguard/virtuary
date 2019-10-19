package com.virtuary.app.firebase

import kotlinx.serialization.Serializable

@Serializable
data class ItemSerializable(
    val documentId: String? = null,
    var name: String? = null,
    var originalLocation: String? = null,
    var currentLocation: String? = null,
    var story: String? = null,
    var relations: List<String>? = null,
    var image: String? = null
)
