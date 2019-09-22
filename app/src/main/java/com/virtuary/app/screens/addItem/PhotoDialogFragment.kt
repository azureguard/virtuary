package com.virtuary.app.screens.addItem

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.virtuary.app.R

class PhotoDialogFragment : DialogFragment() {

    internal lateinit var listener: PhotoDialogListener

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    interface PhotoDialogListener {
        fun onDialogCameraClick(dialog: DialogFragment)
        fun onDialogGalleryClick(dialog: DialogFragment)
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
            //TODO: Change to material alert dialog
            // for now it can't be implemented since it needs Theme.MaterialComponents to be implemented in app theme

            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.add_item_dialog_take_picture_description))
                .setTitle(R.string.add_item_dialog_take_picture)
                .setPositiveButton(
                    R.string.add_item_dialog_camera
                ) { _, _ ->
                    listener.onDialogCameraClick(this)
                }
                .setNegativeButton(
                    R.string.add_item_dialog_gallery
                ) { _, _ ->
                    listener.onDialogGalleryClick(this)
                }
            // Create the AlertDialog object and return it
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
