package com.virtuary.app.screens.addItem

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

class AddItemFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        val binding: FragmentAddItemBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_add_item, container, false)

        // get the home view model
        // assign for databinding so the data in view model can be accessed
        val viewModel: AddItemViewModel = ViewModelProviders.of(this).get(AddItemViewModel::class.java)
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

        binding.spinner.adapter = ArrayAdapter<String>(activity!!,
                                                R.layout.support_simple_spinner_dropdown_item,
                                                viewModel.selectionRelatedTo.value!!)

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.onItemSelected(binding.spinner.selectedItemPosition)
                binding.spinner.setSelection(0)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }

        viewModel.addedRelatedTo.observe(this,
            Observer<MutableList<String>> { data ->
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

                chipGroup.removeAllViews()

                for (chip in children) {
                    chipGroup.addView(chip as View)
                }
            })

        return binding.root
    }
    
}