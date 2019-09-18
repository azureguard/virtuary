package com.virtuary.app.screens.family.member

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberBinding


/**
 * Fragment for the family member details
 */
class MemberFragment : Fragment() {

    private lateinit var memberViewModel: MemberViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentFamilyMemberBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_family_member, container, false
        )

        // get the home view model
        // assign for databinding so the data in view model can be accessed
        memberViewModel = ViewModelProviders.of(this).get(MemberViewModel::class.java)
        binding.memberViewModel = memberViewModel

        // assign adapter so all item list behave the same way
        binding.rvMemberItemList.adapter = MemberItemAdapter(
            memberViewModel.artifactsTitle
        )
        binding.rvMemberItemList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        return binding.root
    }
}
