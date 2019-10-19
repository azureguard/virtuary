package com.virtuary.app.firebase

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @DocumentId val documentId: String? = null,
    var name: String? = null,
    var image: String? = null,
    var alias: Map<String, String>? = null,
    var item: Map<String, Item>? = null
) : Parcelable
