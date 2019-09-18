package com.virtuary.app.screens.family.member

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyMemberBinding


/**
 * Fragment for the family member details
 */
class MemberFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentFamilyMemberBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_family_member, container, false
        )

        return binding.root
    }

}
