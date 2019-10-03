package com.virtuary.app.screens.addItem

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
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentAddItemBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.util.hideKeyboard
import java.io.File
import java.util.*

class AddItemFragment : Fragment(), PhotoDialogFragment.PhotoDialogListener {

    private lateinit var binding: FragmentAddItemBinding

    private val addItemViewModel by viewModels<AddItemViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_item, container, false
        )

        // assign for databinding so the data in view model can be accessed
        binding.addItemViewModel = addItemViewModel

        // can observe LiveData updates
        binding.lifecycleOwner = this

        // sets up event listening to show error when the title is empty
        addItemViewModel.emptyTitle.observe(this, Observer { invalid ->
            if (invalid) {
                binding.addItemTitleLayout.error = "Enter title"
                binding.addItemTitleLayout.isErrorEnabled = true
            } else {
                binding.addItemTitleLayout.isErrorEnabled = false
            }
        })

        // build the drop down add item menu
        binding.addItemRelatedToSpinner.adapter = ArrayAdapter<String>(
            activity!!,
            R.layout.support_simple_spinner_dropdown_item,
            addItemViewModel.selectionRelatedTo.value!!
        )

        binding.addItemRelatedToSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (addItemViewModel.onItemSelected(binding.addItemRelatedToSpinner.selectedItemPosition)) {
                        // set selection to "-" if item selected to avoid confusion in manipulating array
                        binding.addItemRelatedToSpinner.setSelection(0)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }
            }

        addItemViewModel.addedRelatedTo.observe(this,
            Observer<MutableList<String>> { data ->
                // make chip view for each item in the list
                val chipGroup = binding.addItemRelatedToList
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
                        addItemViewModel.onRemoveClick(chipName)
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

        addItemViewModel.document.observe(this,
            Observer<DocumentSnapshot> {
                if (it != null) {
                    hideKeyboard()
                    findNavController().navigate(
                        AddItemFragmentDirections.actionAddItemFragmentToItemFragment(
                            addItemViewModel.document.value?.toObject<Item>()!!
                        )
                    )
                }
            })

        // Show dialog when the add photo is clicked
        binding.addItemImageIcon.setOnClickListener {
            showNoticeDialog()
        }

        binding.addItemImage.setOnClickListener {
            if (binding.addItemImageIcon.visibility == View.GONE) {
                showNoticeDialog()
            }
        }

        addItemViewModel.inProgress.observe(this,
            Observer<Boolean> { inProgress ->
                if (inProgress) {
                    hideKeyboard()
                    binding.progressBar.visibility = View.VISIBLE
                    binding.addItemConfirm.isEnabled = false
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.addItemConfirm.isEnabled = true
                }
            })

        addItemViewModel.isError.observe(this,
            Observer<Boolean> { isError ->
                if (isError) {
                    Toast.makeText(activity, "Server error", Toast.LENGTH_LONG).show()
                }
            })

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
            binding.addItemImage.setImageURI(image)
            binding.addItemImageIcon.visibility = View.GONE

            addItemViewModel.image.value = if (Build.VERSION.SDK_INT < 28) {
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
                Intent.createChooser(intent, "Select Picture"),
                REQUEST_SELECT_IMAGE_IN_ALBUM
            )
        }
    }

    private fun takePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, newImagePath())
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
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


    private fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        hideKeyboard()
        val dialog = PhotoDialogFragment()
        dialog.setTargetFragment(this, 0)
        dialog.show(fragmentManager!!, "NoticeDialogFragment")
    }


    // define static properties
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}
