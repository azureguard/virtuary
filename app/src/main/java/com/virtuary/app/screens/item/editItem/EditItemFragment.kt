package com.virtuary.app.screens.item.editItem

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentEditItemBinding
import kotlinx.android.synthetic.main.fragment_edit_item.edit_item_image
import kotlinx.android.synthetic.main.fragment_edit_item.edit_item_image_icon

class EditItemFragment : Fragment() {

    // argument got from navigation action
    private val args: EditItemFragmentArgs by navArgs()

    private val editItemViewModel by viewModels<EditItemViewModel> { EditItemViewModelFactory(args.item) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        val binding: FragmentEditItemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_item, container, false
        )

        // assign for databinding so the data in view model can be accessed
        binding.editItemViewModel = editItemViewModel

        // can observe LiveData updates
        binding.lifecycleOwner = this

        // sets up event listening to show error when the title is empty
        editItemViewModel.emptyTitle.observe(this, Observer { invalid ->
            if (invalid) {
                binding.editItemTitleLayout.error = getString(R.string.error_title_blank)
                binding.editItemTitleLayout.isErrorEnabled = true
            } else {
                binding.editItemTitleLayout.isErrorEnabled = false
            }
        })

        // build the drop down add item menu
        binding.editItemRelatedToSpinner.adapter = ArrayAdapter<String>(
            activity!!,
            R.layout.support_simple_spinner_dropdown_item,
            editItemViewModel.selectionRelatedTo.value!!
        )

        binding.editItemRelatedToSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (editItemViewModel.onItemSelected(binding.editItemRelatedToSpinner.selectedItemPosition)) {
                        // set selection to "-" if item selected to avoid confusion in manipulating array
                        binding.editItemRelatedToSpinner.setSelection(0)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }
            }

        editItemViewModel.addedRelatedTo.observe(this,
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
                        editItemViewModel.onRemoveClick(chipName)
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
        binding.editItemImageIcon.setOnClickListener {
            selectImageInAlbum()
        }

        binding.editItemImage.setOnClickListener {
            if (binding.editItemImageIcon.visibility == View.INVISIBLE) {
                selectImageInAlbum()
            }
        }

        return binding.root
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // result code is OK only when the user selects an image
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                // returns the content URI for the selected Image
                val selectedImage = data!!.data
                edit_item_image_icon.visibility = View.INVISIBLE
                edit_item_image.setImageURI(selectedImage)
            }
    }

    // define static properties
    companion object {
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}
