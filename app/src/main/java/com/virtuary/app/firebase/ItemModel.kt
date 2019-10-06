package com.virtuary.app.firebase

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Item(
    @DocumentId val documentId: String? = null,
    @ServerTimestamp val timestamp: Date? = null,
    var name: String? = null,
    var originalLocation: String? = null,
    var currentLocation: String? = null,
    var whenExist: Date? = null,
    var story: String? = null,
    var relations: List<String>? = null,
    var image: String? = null
) : Parcelable
