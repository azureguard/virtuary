package com.virtuary.app.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel

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
        binding.rvItemList.adapter = ArtifactAdapter(homeViewModel.artifactsTitle, homeViewModel.artifactsRelatedTo, homeViewModel.artifactsLocation)

        return binding.root
    }
}