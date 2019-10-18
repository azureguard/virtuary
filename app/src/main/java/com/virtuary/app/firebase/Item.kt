package com.virtuary.app.firebase

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import java.util.*

@Parcelize
@Serializable
data class Item(
    @DocumentId val documentId: String? = null,
    @ContextualSerialization @ServerTimestamp val timestamp: Date? = null,
    var name: String? = null,
    var originalLocation: String? = null,
    var currentLocation: String? = null,
    @ContextualSerialization var whenExist: Date? = null,
    var story: String? = null,
    var relations: List<String>? = null,
    var image: String? = null
) : Parcelable
