package com.virtuary.app.screens.item

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.virtuary.app.MainActivity
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentItemBinding

class ItemFragment : Fragment() {

    // argument got from navigation action
    private val args: ItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        val binding: FragmentItemBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_item, container, false
        )

        // TODO: change passing data
        binding.itemTitle.text = args.item.name
        binding.itemCurrentLocation.text = args.item.currentLocation
        binding.itemStory.text = args.item.story

        // set options menu
        setHasOptionsMenu(true)

        // get the item view model
        // assign for databinding so the data in view model can be accessed
        val viewModel: ItemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)

        binding.itemViewModel = viewModel
        viewModel.addRelatedTo(args.item.relations)
        // can observe LiveData updates
        binding.lifecycleOwner = this

        viewModel.relatedTo.observe(this,
            Observer<MutableList<String>> { data ->
                // make chip view for each item in the list
                val chipGroup = binding.itemRelatedToList
                val chipInflater = LayoutInflater.from(chipGroup.context)

                val children = data.map { chipName ->
                    val chip =
                        chipInflater.inflate(R.layout.related_to_list, chipGroup, false) as Chip
                    chip.text = chipName
                    chip.isCloseIconVisible = false
                    chip.isClickable = false
                    chip.isCheckable = false

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

        // Set the action bar label to the clicked item name
        (activity as MainActivity).setActionBarTitle(args.item.name!!)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_edit_item -> {
                // pass title to the edit item fragment
                findNavController().navigate(
                    ItemFragmentDirections.actionItemFragmentToEditItemFragment(
                        args.item.name!!
                    )
                )
            }
            R.id.nav_remove_item -> {
                // TODO: change remove button behaviour
                Log.i("RemoveItem", "Remove Item Menu Button clicked")
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
