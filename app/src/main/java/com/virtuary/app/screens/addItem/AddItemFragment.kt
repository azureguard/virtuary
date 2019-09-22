package com.virtuary.app.screens.addItem

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentAddItemBinding
import kotlinx.android.synthetic.main.fragment_add_item.add_item_image
import kotlinx.android.synthetic.main.fragment_add_item.add_item_image_icon

class AddItemFragment : Fragment(), PhotoDialogFragment.PhotoDialogListener {

    private lateinit var binding: FragmentAddItemBinding

    private lateinit var addItemViewModel: AddItemViewModel

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

        // get the add item view model
        // assign for databinding so the data in view model can be accessed
        addItemViewModel =
            ViewModelProviders.of(this).get(AddItemViewModel::class.java)
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

        // Show dialog when the add photo is clicked
        binding.addItemImageIcon.setOnClickListener {
            showNoticeDialog()
        }

        binding.addItemImage.setOnClickListener {
            if (binding.addItemImageIcon.visibility == View.GONE) {
                showNoticeDialog()
            }
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
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                // TODO: Do something with the selected image from gallery here
                // returns the content URI for the selected Image
                val selectedImage = data!!.data
                add_item_image_icon.visibility = View.GONE
                add_item_image.setImageURI(selectedImage)
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {

                // TODO: Do something with the captured image from camera here
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.addItemImage.setImageBitmap(imageBitmap)
                binding.addItemImageIcon.visibility = View.GONE
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
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = PhotoDialogFragment()
        dialog.setTargetFragment(this,0)
        dialog.show(fragmentManager!!, "NoticeDialogFragment")
    }


    // define static properties
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}
