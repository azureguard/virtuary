package com.virtuary.app.screens.item.addEditItem

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.virtuary.app.MainActivity
import com.virtuary.app.MainActivityViewModel
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentAddEditItemBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.util.*

class AddEditItemFragment : Fragment(),
    PhotoDialogFragment.PhotoDialogListener {

    // argument got from navigation action
    private val args: AddEditItemFragmentArgs by navArgs()

    internal lateinit var binding: FragmentAddEditItemBinding
    private lateinit var selectPhotoHelper: SelectPhotoHelper
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var viewModel: AddEditItemViewModel

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

        mainActivityViewModel =
            ViewModelProviders.of(activity!!).get(MainActivityViewModel::class.java)

        viewModel = BaseViewModelFactory {
            AddEditItemViewModel(
                args.item, mainActivityViewModel.userDB
            )
        }.create(AddEditItemViewModel::class.java)

        selectPhotoHelper = SelectPhotoHelper(context, fragmentManager, this)

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
        viewModel.selectionRelatedTo.observe(this, Observer {
            binding.editItemRelatedToSpinner.adapter = ArrayAdapter(
                activity!!,
                R.layout.support_simple_spinner_dropdown_item,
                it.map {id ->
                    if (id != "Please select here") {
                        mainActivityViewModel.userDB[id]!!.name
                    } else {
                        id
                    }
                }
            )
        })

        binding.editItemRelatedToSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (viewModel.onItemSelected(binding.editItemRelatedToSpinner.selectedItemPosition)) {
                        // set selection to "Please select here" if item selected to avoid confusion in manipulating array
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
                    if (chipName != "Please select here") {
                        chip.text = mainActivityViewModel.userDB[chipName]!!.name
                    } else {
                        chip.text = chipName
                    }
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
                        AddEditItemFragmentDirections.actionEditItemFragmentPop(it)
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
            selectPhotoHelper.showPhotoDialog()
        }

        binding.editItemImage.setOnClickListener {
            if (binding.editItemImageIcon.visibility == View.GONE) {
                selectPhotoHelper.showPhotoDialog()
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
                    GlideApp.with(context!!).load(it).placeholder(R.drawable.ic_launcher_foreground)
                        .centerCrop().into(binding.editItemImage)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // result code is OK only when the user selects an image
        val image = selectPhotoHelper.getResult(requestCode, resultCode, data)
        if (image != null) {
            GlideApp.with(context!!).load(image).centerCrop().into(binding.editItemImage)
            binding.editItemImageIcon.visibility = View.GONE
            viewModel.image.value = selectPhotoHelper.getBitmapFromUri(image)
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
}
