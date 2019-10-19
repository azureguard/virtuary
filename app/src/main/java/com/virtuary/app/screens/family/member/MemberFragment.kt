package com.virtuary.app.screens.family.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberBinding
import com.virtuary.app.firebase.Item
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.util.GlideApp


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

        // To enable the option menu for set alias
        setHasOptionsMenu(true)

        // Set the name by the argument passed from navigation
        binding.memberName.text = args.user.name

        GlideApp.with(this)
            .load(StorageRepository().getImage(args.user.image))
            .placeholder(R.drawable.ic_no_image)
            .centerCrop()
            .into(binding.memberPicture)

        val userItems = args.user.item

        if (userItems == null || userItems.keys.isEmpty()) {
            binding.noItemText.visibility = View.VISIBLE
            binding.rvMemberItemList.visibility = View.GONE
            binding.showAllButton.visibility = View.GONE
        } else {
            binding.noItemText.visibility = View.GONE
            binding.rvMemberItemList.visibility = View.VISIBLE
            binding.showAllButton.visibility = View.VISIBLE
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_app_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun relatedItemOnClick(item : Item){
        findNavController().navigate(
            MemberFragmentDirections.actionGlobalItemFragment(
                item
            )
        )
    }
}
