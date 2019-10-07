package com.virtuary.app.firebase

import android.graphics.Bitmap
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream
import java.util.*

class StorageRepository {
    private val storage = Firebase.storage

    companion object {
        const val ITEM_IMAGE_DIR = "itemImage/"
    }

    fun uploadImage(image: Bitmap): UploadTask {
        val uploadRef = storage.reference.child(ITEM_IMAGE_DIR + UUID.randomUUID() + ".webp")
        val byteArray = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.WEBP, 80, byteArray)
        val data = byteArray.toByteArray()
        val metadata = storageMetadata {
            contentType = "image/webp"
        }
        return uploadRef.putBytes(data, metadata)
    }
}