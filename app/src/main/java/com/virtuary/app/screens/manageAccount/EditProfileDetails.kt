package com.virtuary.app.screens.manageAccount

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentEditProfileDetailsBinding
import com.virtuary.app.util.GlideApp
import com.virtuary.app.util.PhotoDialogFragment
import com.virtuary.app.util.SelectPhotoHelper
import com.virtuary.app.util.hideKeyboard

class EditProfileDetails : Fragment(),
    PhotoDialogFragment.PhotoDialogListener,
    RequestPasswordDialogFragment.RequestPasswordDialogListener {
    private val viewModel by viewModels<EditProfileDetailsViewModel>()

    internal lateinit var binding: FragmentEditProfileDetailsBinding
    private lateinit var selectPhotoHelper: SelectPhotoHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_profile_details,
            container,
            false
        )

        selectPhotoHelper = SelectPhotoHelper(context, fragmentManager, this)

        binding.editProfileViewModel = viewModel
        binding.lifecycleOwner = this

        binding.frameName.setOnClickListener {
            createDialog(context!!, resources.getString(R.string.name))
        }
        binding.frameEmail.setOnClickListener {
            // showReAuthenticationDialog()
            createDialog(context!!, resources.getString(R.string.email))
        }
        binding.editProfileImage.setOnClickListener {
            selectPhotoHelper.showPhotoDialog()
        }

        viewModel.profileImage.observe(
            this,
            Observer {
                if (it != null) {
                    GlideApp.with(context!!).load(it).error(R.drawable.ic_launcher_foreground)
                        .circleCrop().into(binding.profilePicture)
                }
            })

        viewModel.uploading.observe(this, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.editProfileImage.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.editProfileImage.isEnabled = true
            }
        })

        viewModel.authRequired.observe(this, Observer {
            if (it) {
                showReAuthenticationDialog()
            }
        })

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
                SpannableStringBuilder(viewModel.name.value)
            resources.getString(R.string.email) -> input.text =
                SpannableStringBuilder(viewModel.email.value)
        }

        // Create the alert dialog
        builder.apply {
            setTitle(title)
            setView(viewInflated)
            setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                when (title) {
                    resources.getString(R.string.name) -> viewModel.updateName(input.text.toString())
                    resources.getString(R.string.email) -> viewModel.updateEmail(input.text.toString())
                }
                dialog.dismiss()
            }
            setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ -> dialog.cancel() }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // result code is OK only when the user selects an image
        val image = selectPhotoHelper.getResult(requestCode, resultCode, data)
        if (image != null) {
            GlideApp.with(context!!).load(image).circleCrop().into(binding.profilePicture)
            viewModel.image.value = selectPhotoHelper.getBitmapFromUri(image)
            viewModel.updateImage()
        }
    }

    override fun onDialogCameraClick() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectPhotoHelper.newImagePath())
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(
                    takePictureIntent,
                    SelectPhotoHelper.REQUEST_IMAGE_CAPTURE
                )
            }
        }
    }

    override fun onDialogGalleryClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // check if the Activity component is available to handle the intent
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(
                Intent.createChooser(intent, context?.getString(R.string.select_picture)),
                SelectPhotoHelper.REQUEST_SELECT_IMAGE_IN_ALBUM
            )
        }
    }

    private fun showReAuthenticationDialog() {
        hideKeyboard()
        val dialog = RequestPasswordDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager!!, null)
    }

    override fun onDialogAuth(password: String, callback: (result: Boolean) -> Unit) {
        viewModel.verifyPassword(password, callback)
    }
}
