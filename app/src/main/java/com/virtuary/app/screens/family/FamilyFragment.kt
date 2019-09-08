package com.virtuary.app.screens.family

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentFamilyBinding

class FamilyFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFamilyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_family, container,false)

        // create family view model
        val familyViewModel = ViewModelProviders.of(this).get(FamilyViewModel::class.java)
        binding.familyViewModel = familyViewModel

        // implement grid layout
        binding.rvFamilyList.layoutManager = GridLayoutManager(activity, 2)

        // assign family list adapter
        binding.rvFamilyList.adapter?.setHasStableIds(true)
        binding.rvFamilyList.adapter = FamilyAdapter(familyViewModel.familyMemberName)

        return binding.root
    }
}
