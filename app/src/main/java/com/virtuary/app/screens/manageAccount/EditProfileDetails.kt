package com.virtuary.app.screens.manageAccount

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.virtuary.app.R
import android.content.Context
import android.content.DialogInterface
import android.text.SpannableStringBuilder
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.virtuary.app.databinding.FragmentEditProfileDetailsBinding


class EditProfileDetails : Fragment() {

    companion object {
        fun newInstance() = EditProfileDetails()
    }

    private lateinit var viewModel: EditProfileDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditProfileDetailsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_profile_details, container, false
        )
        binding.frameName.setOnClickListener {
            createDialog(context!!, resources.getString(R.string.name))
        }
        binding.frameEmail.setOnClickListener {
            createDialog(context!!, resources.getString(R.string.email))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun createDialog(context: Context, title: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        val viewInflated = LayoutInflater.from(context)
            .inflate(R.layout.dialog_edit_profile, view as ViewGroup?, false)

        // Set up the input
        val input = viewInflated.findViewById(R.id.input) as EditText
        when (title) {
            resources.getString(R.string.name) -> input.text = SpannableStringBuilder(viewModel.name.get()!!)
            resources.getString(R.string.email) -> input.text = SpannableStringBuilder(viewModel.email.get()!!)
        }

        builder.setView(viewInflated)

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok
        ) { dialog, which ->
            dialog.dismiss()
        }
        builder.setNegativeButton(android.R.string.cancel
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

}
