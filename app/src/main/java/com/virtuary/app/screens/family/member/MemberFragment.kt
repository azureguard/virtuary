package com.virtuary.app.screens.family.member

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberBinding


/**
 * Fragment for the family member details
 */
class MemberFragment : Fragment() {

    private lateinit var memberViewModel: MemberViewModel

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
        binding.memberName.text = args.name

        // get the home view model
        // assign for databinding so the data in view model can be accessed
        memberViewModel = ViewModelProviders.of(this).get(MemberViewModel::class.java)
        binding.memberViewModel = memberViewModel

        // Conditional rendering depending on the item number that the specific member have
        memberViewModel.itemSize.observe(this, Observer { size ->
            if (size <= 0) {
                binding.noItemText.visibility = View.VISIBLE
                binding.rvMemberItemList.visibility = View.GONE
            } else {
                binding.noItemText.visibility = View.GONE
                binding.rvMemberItemList.visibility = View.VISIBLE
            }
        })

        // assign adapter so all item list behave the same way
        binding.rvMemberItemList.adapter = MemberItemAdapter(
            memberViewModel.artifactsTitle
        )
        binding.rvMemberItemList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        binding.showAllButton.setOnClickListener {
            // TODO : for now pass name to the next fragment,
            //  It may be changed to pass ID or add ID for other argument (need to change the argument in main_navigation.xml)
            findNavController().navigate(
                MemberFragmentDirections.actionMemberFragmentToMemberItemFragment(
                    args.name
                )
            )
        }

        return binding.root
    }
}
