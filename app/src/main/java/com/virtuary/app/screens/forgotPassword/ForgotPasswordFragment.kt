package com.virtuary.app.screens.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentForgotPasswordBinding


/**
 * Fragment for the Forgot Password screen of the app
 */
class ForgotPasswordFragment : Fragment() {

    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentForgotPasswordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_forgot_password, container, false
        )

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.forgotPasswordViewModel = viewModel

        viewModel.getIsSuccess().observe(
            this,
            Observer<Boolean> { isSuccess ->
                if (isSuccess) findNavController().navigate(
                    ForgotPasswordFragmentDirections.actionForgotPasswordFragmentPop()
                )
            })

        return binding.root
    }
}
