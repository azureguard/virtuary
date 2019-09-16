package com.virtuary.app.screens.manageAccount

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.virtuary.app.R

class EditProfileDetails : Fragment() {

    companion object {
        fun newInstance() = EditProfileDetails()
    }

    private lateinit var viewModel: EditProfileDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
