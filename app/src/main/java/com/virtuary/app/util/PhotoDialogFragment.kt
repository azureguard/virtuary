package com.virtuary.app.util

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.virtuary.app.R

class PhotoDialogFragment : DialogFragment() {

    internal lateinit var listener: PhotoDialogListener

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    interface PhotoDialogListener {
        fun onDialogCameraClick()
        fun onDialogGalleryClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        try {
            // Instantiate the PhotoDialogListener so we can send events to the host
            listener = targetFragment as PhotoDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (targetFragment.toString() +
                        " must implement PhotoDialogListener")
            )
        }

        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = MaterialAlertDialogBuilder(it)

            builder.setMessage(getString(R.string.add_item_dialog_add_picture_description))
                .setTitle(R.string.add_item_dialog_add_picture)
                .setPositiveButton(
                    R.string.add_item_dialog_camera
                ) { _, _ ->
                    listener.onDialogCameraClick()
                }
                .setNegativeButton(
                    R.string.add_item_dialog_gallery
                ) { _, _ ->
                    listener.onDialogGalleryClick()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
