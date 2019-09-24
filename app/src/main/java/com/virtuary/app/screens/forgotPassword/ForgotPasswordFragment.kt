package com.virtuary.app.screens.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentForgotPasswordBinding
import com.virtuary.app.util.hideKeyboard


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

        viewModel.invalidEmail.observe(this, Observer { invalid ->
            if (invalid) {
                binding.email.error = "Please enter a valid email"
                binding.email.isErrorEnabled = true
            } else {
                binding.email.isErrorEnabled = false
            }
        })
        binding.emailEdit.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) binding.email.isErrorEnabled = false
            }
        }

        viewModel.isSuccess.observe(
            this,
            Observer<Boolean> { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(activity, "Password reset email sent", Toast.LENGTH_LONG).show()
                    findNavController().navigate(
                        ForgotPasswordFragmentDirections.actionForgotPasswordFragmentPop()
                    )
                } else {
                    binding.email.error = "There is no account associated with this email"
                    binding.email.isErrorEnabled = true
                }
            })

        viewModel.inProgress.observe(this, Observer<Boolean> { inProgress ->
            run {
                if (inProgress) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.sendButton.isEnabled = false
                    hideKeyboard()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.sendButton.isEnabled = true
                }
            }
        })

        return binding.root
    }
}
