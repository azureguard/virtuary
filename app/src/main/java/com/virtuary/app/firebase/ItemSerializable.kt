package com.virtuary.app.firebase

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import kotlinx.serialization.Serializable

@Serializable
data class ItemSerializable(
    var name: String? = null,
    var originalLocation: String? = null,
    var currentLocation: String? = null,
    var story: String? = null,
    var relations: List<String>? = null,
    var image: String? = null,
    override val objectID: ObjectID
) : Indexable
