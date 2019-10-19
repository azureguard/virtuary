package com.virtuary.app.screens.family.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.virtuary.app.MainActivityViewModel
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberBinding
import com.virtuary.app.firebase.Item


/**
 * Fragment for the family member details
 */
class MemberFragment : Fragment() {

    // argument got from navigation action
    private val args: MemberFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentFamilyMemberBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_family_member, container, false
        )

        // Set the name by the argument passed from navigation
        binding.memberName.text = args.user.name

        val userItems = args.user.item

        if (userItems == null || userItems.keys.isEmpty()) {
            binding.noItemText.visibility = View.VISIBLE
            binding.rvMemberItemList.visibility = View.GONE
        } else {
            binding.noItemText.visibility = View.GONE
            binding.rvMemberItemList.visibility = View.VISIBLE
        }

        val userItem = mutableListOf<Item>()
        var itemCount = 0
        if (userItems != null) {
            for (item in userItems.values) {
                itemCount++
                userItem.add(item)
                if (itemCount == 5) {
                    break
                }
            }
        }

        // assign adapter so all item list behave the same way
        val adapter = MemberItemAdapter(::relatedItemOnClick, this)
        binding.rvMemberItemList.adapter = adapter
        adapter.submitList(userItem)

        binding.rvMemberItemList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        binding.showAllButton.setOnClickListener {
            findNavController().navigate(
                MemberFragmentDirections.actionMemberFragmentToMemberItemFragment(
                    args.user
                )
            )
        }

        return binding.root
    }

    private fun relatedItemOnClick(item: Item) {
        findNavController().navigate(
            MemberFragmentDirections.actionGlobalItemFragment(
                item
            )
        )
    }
}
