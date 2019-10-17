package com.virtuary.app.screens.item

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.virtuary.app.MainActivity
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentItemBinding
import com.virtuary.app.util.BaseViewModelFactory
import com.virtuary.app.util.GlideApp

class ItemFragment : Fragment() {

    // argument got from navigation action
    private val args: ItemFragmentArgs by navArgs()
    internal val viewModel: ItemViewModel by viewModels { BaseViewModelFactory { ItemViewModel(args.item) } }

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

        binding.itemTitle.text = args.item.name
        binding.itemCurrentLocation.text = args.item.currentLocation
        binding.itemStory.text = args.item.story

        // set options menu
        setHasOptionsMenu(true)

        // assign for databinding so the data in view model can be accessed
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

        viewModel.itemImage.observe(
            this,
            Observer {
                if (it != null) {
                    GlideApp.with(context!!).load(it).fallback(R.drawable.ic_no_image).centerCrop()
                        .into(binding.itemImage)
                }
            })

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
                        args.item
                    )
                )
            }
            R.id.nav_remove_item -> {
                viewModel.deleteItem(args.item)
                findNavController().navigate(ItemFragmentDirections.actionGlobalHomeFragment())
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
