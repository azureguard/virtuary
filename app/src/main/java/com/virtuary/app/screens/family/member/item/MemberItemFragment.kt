package com.virtuary.app.screens.family.member.item

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.virtuary.app.MainActivity
import com.virtuary.app.MainActivityViewModel
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberItemBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.screens.home.ItemAdapter

/**
 * Fragment for the family member items
 */
class MemberItemFragment : Fragment(), SearchView.OnQueryTextListener {

    // argument got from navigation action
    private val args: MemberItemFragmentArgs by navArgs()

    private lateinit var binding: FragmentFamilyMemberItemBinding

    private lateinit var searchView: SearchView

    private val mainActivityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_family_member_item,
            container,
            false
        )

        var userName = args.user.name

        if (args.user.alias != null && args.user.alias!!.containsKey(mainActivityViewModel.currentUser)) {
            userName = args.user.alias!![mainActivityViewModel.currentUser]
        }

        // Dynamically change the label on the action bar
        (activity as MainActivity).setActionBarTitle(
            String.format(
                getString(R.string.possessive_items),
                userName
            )
        )

        // assign adapter so all item list behave the same way
        val adapter = ItemAdapter(this, mainActivityViewModel)
        binding.rvItemList.adapter = adapter

        val userItemMap = args.user.item
        val userItems = mutableListOf<Item>()
        if (userItemMap != null) {
            for (item in userItemMap.values) {
                userItems.add(item)
            }
        }

        // check difference between the new list against the old one
        // run all the needed changes on the recycler view
        // will detect any items that were added, removed, changed, updated the items shown by recycler view
        adapter.submitList(userItems)

        // To indicate there's option button other than up or hamburger button in action bar
        setHasOptionsMenu(true)

        // Change the behaviour of onBackPressed to handle closing search bar
        // https://stackoverflow.com/questions/51043428/handling-back-button-in-android-navigation-component
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                if (searchView.isIconified) {
                    findNavController().popBackStack()
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
        searchView.queryHint = getString(R.string.member_item_search_hint)
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
