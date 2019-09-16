package com.virtuary.app.screens.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentEditAccountBinding

class EditAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditAccountBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_account, container, false
        )
        binding.editAccount.setOnClickListener {
            findNavController().navigate(EditAccountFragmentDirections.actionEditAccountToEditProfileDetails())
        }
        binding.changePassword.setOnClickListener {
            findNavController().navigate(EditAccountFragmentDirections.actionEditAccountToChangePassword())
        }
        binding.forgotPassword.setOnClickListener {
            TODO()
        }

        return binding.root
    }
}
