package com.virtuary.app.screens.addItem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentAddItemBinding
import kotlinx.android.synthetic.main.fragment_add_item.add_item_image
import kotlinx.android.synthetic.main.fragment_add_item.add_item_image_icon

class AddItemFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        val binding: FragmentAddItemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_item, container, false
        )

        // get the home view model
        // assign for databinding so the data in view model can be accessed
        val viewModel: AddItemViewModel =
            ViewModelProviders.of(this).get(AddItemViewModel::class.java)
        binding.addItemViewModel = viewModel

        // can observe LiveData updates
        binding.lifecycleOwner = this

        // sets up event listening to show error when the title is empty
        viewModel.emptyTitle.observe(this, Observer { invalid ->
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
            viewModel.selectionRelatedTo.value!!
        )



        binding.addItemRelatedToSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (viewModel.onItemSelected(binding.addItemRelatedToSpinner.selectedItemPosition)) {
                    // set selection to "-" if item selected to avoid confusion in manipulating array
                    binding.addItemRelatedToSpinner.setSelection(0)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }

        viewModel.addedRelatedTo.observe(this,
            Observer<MutableList<String>> { data ->
                // make chip view for each item in the list
                val chipGroup = binding.addItemRelatedToList
                val inflator = LayoutInflater.from(chipGroup.context)

                val children = data.map { chipName ->
                    val chip = inflator.inflate(R.layout.related_to_list, chipGroup, false) as Chip
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

        // sets click listener for adding image in gallery
        binding.addItemImageIcon.setOnClickListener {
            selectImageInAlbum()
        }

        binding.addItemImage.setOnClickListener {
            if (binding.addItemImageIcon.visibility == View.INVISIBLE) {
                selectImageInAlbum()
            }
        }

        return binding.root
    }

    // Reference from https://stackoverflow.com/questions/44148883/select-image-from-gallery-using-kotlin
    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        // check if the Activity component is available to handle the intent
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                                    REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

//    private fun takePhoto() {
//        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent1.resolveActivity(context!!.packageManager) != null) {
//            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // result code is OK only when the user selects an image
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM){
                // returns the content URI for the selected Image
                val selectedImage = data!!.data
                add_item_image_icon.visibility = View.INVISIBLE
                add_item_image.setImageURI(selectedImage)
            }
    }

    // define static properties
    companion object {
//        private val REQUEST_TAKE_PHOTO = 0
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}