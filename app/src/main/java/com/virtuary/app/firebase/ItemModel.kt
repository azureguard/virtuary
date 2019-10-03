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
    val name: String? = null,
    val originalLocation: String? = null,
    val currentLocation: String? = null,
    val whenExist: Date? = null,
    val story: String? = null,
    val relations: List<String>? = null,
    val pictures: List<String>? = null
) : Parcelable
