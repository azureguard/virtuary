package com.virtuary.app.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentLoginBinding


/**
 * Fragment for the starting screen of the app
 */
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)

        // Set up button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the viewmodel
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.loginViewModel = viewModel

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // Sets up event listening to show error when the email is invalid
        viewModel.invalidEmail.observe(this, Observer { invalid ->
            if (invalid) {
                binding.emailEdit.error = "Invalid Email"
                binding.emailEdit.isErrorEnabled = true
            } else {
                binding.emailEdit.isErrorEnabled = false
            }
        })

        // Sets up event listening to show error when the password is invalid
        viewModel.invalidPassword.observe(this, Observer { invalid ->
            if(invalid) {
                binding.passwordEdit.error = "Invalid Password"
                binding.passwordEdit.isErrorEnabled = true
            } else {
                binding.passwordEdit.isErrorEnabled = false
            }
        })

        return binding.root
    }
}
