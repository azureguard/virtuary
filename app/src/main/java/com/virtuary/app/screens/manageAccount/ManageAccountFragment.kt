package com.virtuary.app.screens.manageAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentManageAccountBinding

class ManageAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentManageAccountBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_manage_account, container, false
        )
        binding.editAccount.setOnClickListener {
            findNavController().navigate(ManageAccountFragmentDirections.actionManageAccountToEditProfileDetails())
        }
        binding.changePassword.setOnClickListener {
            findNavController().navigate(ManageAccountFragmentDirections.actionManageAccountToChangePassword())
        }
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(ManageAccountFragmentDirections.actionManageAccountToForgotPasswordFragment())
        }

        return binding.root
    }
}
