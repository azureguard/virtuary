package com.virtuary.app.screens.family.member.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.virtuary.app.MainActivity
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberItemBinding
import com.virtuary.app.screens.home.ArtifactAdapter

/**
 * Fragment for the family member items
 */
class MemberItemFragment : Fragment() {

    // argument got from navigation action
    private val args: MemberItemFragmentArgs by navArgs()

    private lateinit var binding: FragmentFamilyMemberItemBinding

    private lateinit var memberItemViewModel: MemberItemViewModel

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

        // Dynamically change the label on the action bar
        (activity as MainActivity).setActionBarTitle("${args.name}'s items")

        // get the home view model
        // assign for databinding so the data in view model can be accessed
        memberItemViewModel = ViewModelProviders.of(this).get(MemberItemViewModel::class.java)
        binding.memberItemViewModel = memberItemViewModel

        // assign adapter so all item list behave the same way
        binding.rvItemList.adapter = ArtifactAdapter(
            memberItemViewModel.artifactsTitle,
            memberItemViewModel.artifactsRelatedTo,
            memberItemViewModel.artifactsLocation
        )

        return binding.root
    }
}
