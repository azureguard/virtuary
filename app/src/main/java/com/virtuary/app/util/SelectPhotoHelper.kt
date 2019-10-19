package com.virtuary.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.io.File
import java.util.*

class SelectPhotoHelper(
    private val context: Context?,
    private val fragmentManager: FragmentManager?,
    private val parent: Fragment
) {
    private var imageUri: Uri? = null

    private fun getImagePath(): Uri? {
        return imageUri ?: generate()
    }

    fun newImagePath(): Uri? {
        return generate()
    }

    private fun generate(): Uri? {
        val storageDir = context?.externalCacheDir
        val image = File(storageDir, UUID.randomUUID().toString() + ".jpg")
        imageUri = FileProvider.getUriForFile(
            context!!, (context.applicationContext
            !!.packageName) + ".provider", image
        )
        return imageUri
    }

    fun showPhotoDialog() {
        // Create an instance of the dialog fragment and show it
        parent.hideKeyboard()
        val dialog = PhotoDialogFragment()
        dialog.setTargetFragment(parent, 0)
        dialog.show(fragmentManager!!, null)
    }

    fun getResult(requestCode: Int, resultCode: Int, data: Intent?): Uri? {
        // result code is OK only when the user selects an image
        return if (resultCode == Activity.RESULT_OK) {
            var image: Uri? = null
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                // returns the content URI for the selected Image
                image = data?.data
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                image = getImagePath()
            }
            image
        } else {
            null
        }
    }

    fun getBitmapFromUri(image: Uri?): Bitmap? {
        return if (Build.VERSION.SDK_INT < 28) {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(
                context?.contentResolver,
                image
            )
        } else {
            val source = ImageDecoder.createSource(context?.contentResolver!!, image!!)
            ImageDecoder.decodeBitmap(source)
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 0
        const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}
