package com.virtuary.app.screens.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.virtuary.app.MainActivity
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // get the home view model
        // assign for databinding so the data in view model can be accessed
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.homeViewModel = homeViewModel

        // assign adapter so all item list behave the same way
        binding.rvItemList.adapter = ArtifactAdapter(
            homeViewModel.artifactsTitle,
            homeViewModel.artifactsRelatedTo,
            homeViewModel.artifactsLocation,
            this
        )

        binding.addItemButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddItemFragment())
        }

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
        searchView.queryHint = getString(R.string.home_search_hint)
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
}
