package com.virtuary.app.screens.manageAccount

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentEditProfileDetailsBinding


class EditProfileDetails : Fragment() {
    private val viewModel by viewModels<EditProfileDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditProfileDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_profile_details, container, false
        )

        binding.editProfileViewModel = viewModel
        binding.frameName.setOnClickListener {
            createDialog(context!!, resources.getString(R.string.name))
        }
        binding.frameEmail.setOnClickListener {
            createDialog(context!!, resources.getString(R.string.email))
        }

        return binding.root
    }

    private fun createDialog(context: Context, title: String) {
        val builder = MaterialAlertDialogBuilder(context)
        val viewInflated = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_profile, view as ViewGroup?, false)

        // Set up the input
        val input = viewInflated.findViewById(R.id.input) as EditText
        when (title) {
            resources.getString(R.string.name) -> input.text =
                SpannableStringBuilder(viewModel.name.get())
            resources.getString(R.string.email) -> input.text =
                SpannableStringBuilder(viewModel.email.get())
        }

        // Create the alert dialog
        builder.apply {
            setTitle(title)
            setView(viewInflated)
            setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                when (title) {
                    resources.getString(R.string.name) -> viewModel.name.set(input.text.toString())
                    resources.getString(R.string.email) -> viewModel.email.set(input.text.toString())
                }
                dialog.dismiss()
            }
            setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ -> dialog.cancel() }
        }

        builder.show()
    }
}
