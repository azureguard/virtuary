package com.virtuary.app.screens.manageAccount

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.virtuary.app.R

class RequestPasswordDialogFragment : DialogFragment() {
    internal lateinit var listener: RequestPasswordDialogListener

    interface RequestPasswordDialogListener {
        fun onDialogAuth(password: String, callback: (result: Boolean) -> Unit)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        try {
            // Instantiate the RequestPasswordDialogListener so we can send events to the host
            listener = targetFragment as RequestPasswordDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (targetFragment.toString() +
                    " must implement RequestPasswordDialogListener")
            )
        }
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            val viewInflated = LayoutInflater.from(context)
                .inflate(R.layout.dialog_standard, view as ViewGroup?, false)
            val input = viewInflated.findViewById(R.id.input) as EditText
            val inputLayout = viewInflated.findViewById(R.id.inputLayout) as TextInputLayout
            inputLayout.hint = getString(R.string.profile_current_password)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            input.transformationMethod = PasswordTransformationMethod.getInstance()
            input.doAfterTextChanged { inputLayout.isErrorEnabled = false }

            builder.apply {
                setTitle("Re-authentication required")
                setView(viewInflated)
                setPositiveButton(android.R.string.ok, null)
                setNegativeButton(
                    android.R.string.cancel
                ) { dialog, _ -> dialog.cancel() }
            }
            val dialog = builder.create()

            dialog.setOnShowListener {
                val button = dialog.getButton(Dialog.BUTTON_POSITIVE)
                button.setOnClickListener {
                    listener.onDialogAuth(input.text.toString()) { result ->
                        if (result) {
                            dialog.dismiss()
                        } else {
                            inputLayout.error = getString(R.string.error_invalid_password)
                        }
                    }
                }
            }
            dialog
        } ?: throw IllegalStateException("Failed to create request password dialog")
    }
}
