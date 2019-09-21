package com.virtuary.app.screens.family

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.virtuary.app.MainActivity
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyBinding

class FamilyFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var familyViewModel: FamilyViewModel

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFamilyBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_family, container, false)

        // create family view model
        familyViewModel = ViewModelProviders.of(this).get(FamilyViewModel::class.java)
        binding.familyViewModel = familyViewModel

        // implement grid layout
        binding.rvFamilyList.layoutManager = GridLayoutManager(activity, 2)

        // assign family list adapter
        binding.rvFamilyList.adapter?.setHasStableIds(true)
        binding.rvFamilyList.adapter =
            FamilyAdapter(familyViewModel.familyMemberName, ::memberOnClick)

        // To indicate there's option button other than up or hamburger button in action bar
        setHasOptionsMenu(true)

        // Change the behaviour of onBackPressed to handle closing search bar
        // https://stackoverflow.com/questions/51043428/handling-back-button-in-android-navigation-component
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                if (searchView.isIconified) {
                    requireActivity().finish()
                } else {
                    searchView.isIconified = true
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )

        return binding.root
    }
    // Function to inflate the action bar menu (including search)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.family_search_hint)
        searchView.setOnQueryTextListener(this)

        // Make the search bar fill the entire action bar
        val displayMetrics = DisplayMetrics()
        (activity as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        searchView.maxWidth = displayMetrics.widthPixels
        searchView.setOnSearchClickListener {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        searchView.setOnCloseListener {
            (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    //TODO: Using one of this function, connect it to the viewModel to do search
    // two of the function need to be here, just change one of the function implementation

    // Called when the action bar search text has been submitted using button or others
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    // Called when the action bar search text has changed.
    override fun onQueryTextChange(query: String?): Boolean {
        return true
    }

    // TODO: Change implementation to pass id to re fetch or using other implementation
    private fun memberOnClick(name: String) {
        findNavController().navigate(
            FamilyFragmentDirections.actionFamilyFragmentToMemberFragment(
                name
            )
        )
    }
}
