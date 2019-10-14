package com.virtuary.app.screens.item.addEditItem

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.virtuary.app.MainActivity
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentAddEditItemBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.util.BaseViewModelFactory
import com.virtuary.app.util.GlideApp
import com.virtuary.app.util.PhotoDialogFragment
import com.virtuary.app.util.hideKeyboard
import java.io.File
import java.util.*

class AddEditItemFragment : Fragment(),
    PhotoDialogFragment.PhotoDialogListener {

    // argument got from navigation action
    private val args: AddEditItemFragmentArgs by navArgs()

    internal lateinit var binding: FragmentAddEditItemBinding

    internal val viewModel: AddEditItemViewModel by viewModels {
        BaseViewModelFactory {
            AddEditItemViewModel(
                args.item
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_edit_item, container, false
        )

        // assign for databinding so the data in view model can be accessed
        binding.editItemViewModel = viewModel

        // can observe LiveData updates
        binding.lifecycleOwner = this

        // sets up event listening to show error when the title is empty
        viewModel.emptyTitle.observe(this, Observer { invalid ->
            if (invalid) {
                binding.editItemTitleLayout.error = getString(R.string.error_title_blank)
                binding.editItemTitleLayout.isErrorEnabled = true
            } else {
                binding.editItemTitleLayout.isErrorEnabled = false
            }
        })

        // build the drop down add item menu
        binding.editItemRelatedToSpinner.adapter = ArrayAdapter(
            activity!!,
            R.layout.support_simple_spinner_dropdown_item,
            viewModel.selectionRelatedTo.value!!
        )

        binding.editItemRelatedToSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (viewModel.onItemSelected(binding.editItemRelatedToSpinner.selectedItemPosition)) {
                        // set selection to "-" if item selected to avoid confusion in manipulating array
                        binding.editItemRelatedToSpinner.setSelection(0)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }
            }

        viewModel.addedRelatedTo.observe(this,
            Observer<MutableList<String>> { data ->
                // make chip view for each item in the list
                val chipGroup = binding.editItemRelatedToList
                val chipInflater = LayoutInflater.from(chipGroup.context)

                val children = data.map { chipName ->
                    val chip =
                        chipInflater.inflate(R.layout.related_to_list, chipGroup, false) as Chip
                    chip.text = chipName
                    chip.isCloseIconVisible = true
                    chip.isClickable = false
                    chip.isCheckable = false

                    chip.setOnCloseIconClickListener {
                        chipGroup.removeView(chip as View)
                        viewModel.onRemoveClick(chipName)
                    }

                    chip
                }

                // remove any views already in ChipGroup
                chipGroup.removeAllViews()

                // add the new children to the ChipGroup
                for (chip in children) {
                    chipGroup.addView(chip as View)
                }
            }
        )

        viewModel.document.observe(this, Observer<Item> {
            if (it != null) {
                hideKeyboard()
                if (viewModel.isEdit.value!!) {
                    findNavController().navigate(
                        AddEditItemFragmentDirections.actionEditItemFragmentPop()
                    )
                } else {
                    findNavController().navigate(
                        AddEditItemFragmentDirections.actionEditItemFragmentToItemFragment(it)
                    )
                }
            }
        })

        // Show dialog when the add photo is clicked
        binding.editItemImageIcon.setOnClickListener {
            showPhotoDialog()
        }

        binding.editItemImage.setOnClickListener {
            if (binding.editItemImageIcon.visibility == View.GONE) {
                showPhotoDialog()
            }
        }

        viewModel.inProgress.observe(this,
            Observer<Boolean> { inProgress ->
                if (inProgress) {
                    hideKeyboard()
                    binding.progressBar.visibility = View.VISIBLE
                    binding.editItemConfirm.isEnabled = false
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.editItemConfirm.isEnabled = true
                }
            })

        viewModel.isError.observe(this,
            Observer<Boolean> { isError ->
                if (isError) {
                    Toast.makeText(
                        activity,
                        context?.getText(R.string.error_server_unreachable),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        viewModel.itemImage.observe(
            this,
            Observer {
                if (it != null) {
                    GlideApp.with(context!!).load(it).placeholder(R.drawable.ic_launcher_foreground).centerCrop().into(binding.editItemImage)
                    binding.editItemImageIcon.visibility = View.GONE
                }
            })

        if (args.item == null) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.add_item))
        } else {
            (activity as MainActivity).setActionBarTitle(getString(R.string.edit_item))
        }

        return binding.root
    }

    override fun onDialogCameraClick(dialog: DialogFragment) {
        takePicture()
    }

    override fun onDialogGalleryClick(dialog: DialogFragment) {
        selectImageInAlbum()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // result code is OK only when the user selects an image
        if (resultCode == Activity.RESULT_OK) {
            var image: Uri? = null
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                // returns the content URI for the selected Image
                image = data?.data
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                image = getImagePath()
            }
            GlideApp.with(context!!).load(image).centerCrop().into(binding.editItemImage)
            binding.editItemImageIcon.visibility = View.GONE

            viewModel.image.value = if (Build.VERSION.SDK_INT < 28) {
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
    }

    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // check if the Activity component is available to handle the intent
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.select_picture)),
                REQUEST_SELECT_IMAGE_IN_ALBUM
            )
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, newImagePath())
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(
                    takePictureIntent,
                    REQUEST_IMAGE_CAPTURE
                )
            }
        }
    }

    private var imageUri: Uri? = null

    private fun getImagePath(): Uri? {
        return imageUri ?: generate()
    }

    private fun newImagePath(): Uri? {
        return generate()
    }

    private fun generate(): Uri? {
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val image = File(storageDir, UUID.randomUUID().toString() + ".jpg")
        imageUri = FileProvider.getUriForFile(
            context!!, context!!.applicationContext
                .packageName + ".provider", image
        )
        return imageUri
    }

    private fun showPhotoDialog() {
        // Create an instance of the dialog fragment and show it
        hideKeyboard()
        val dialog = PhotoDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager!!, null)
    }

    // define static properties
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}
