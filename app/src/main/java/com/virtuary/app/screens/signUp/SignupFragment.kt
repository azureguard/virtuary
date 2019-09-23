package com.virtuary.app.screens.signUp

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentSignUpBinding
import com.virtuary.app.util.hideKeyboard


/**
 * Fragment for the Signup screen of the app
 */
class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentSignUpBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container, false)

        // Get the viewmodel
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.signUpViewModel = viewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        // Sets up event listening to show error when the email is invalid
        viewModel.invalidEmail.observe(this, Observer { invalid ->
            if (invalid) {
                binding.emailText.error = "Invalid Email"
                binding.emailText.isErrorEnabled = true
            } else {
                binding.emailText.isErrorEnabled = false
            }
        })
        binding.emailEdit.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) binding.emailText.isErrorEnabled = false
            }
        }

        // Sets up event listening to show error when the password is invalid
        viewModel.invalidPassword.observe(this, Observer { invalid ->
            if(invalid) {
                binding.passwordText.error = "Password need to be of length 6 or more"
                binding.passwordText.isErrorEnabled = true
            } else {
                binding.passwordText.isErrorEnabled = false
            }
        })
        binding.passwordEdit.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) binding.passwordText.isErrorEnabled = false
            }
        }
        // Sets up event listening to show error when the name is invalid
        viewModel.invalidName.observe(this, Observer { invalid ->
            if(invalid) {
                binding.nameText.error = "Please enter your name"
                binding.nameText.isErrorEnabled = true
            } else {
                binding.nameText.isErrorEnabled = false
            }
        })
        binding.nameEdit.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (hasFocus) binding.nameText.isErrorEnabled = false
            }
        }

        binding.passwordEdit.setOnKeyListener { v, keyCode, event ->
            run {
                if (event.keyCode == KeyEvent.KEYCODE_ENTER ||
                    event.action == KeyEvent.ACTION_DOWN ||
                    keyCode == EditorInfo.IME_ACTION_DONE
                ) {
                    hideKeyboard()
                    viewModel.onClick()
                }
                return@run true
            }
        }

        viewModel.getInProgress().observe(this, Observer<Boolean> { inProgress ->
            run {
                if (inProgress) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.signupButton.isEnabled = false
                    hideKeyboard()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.signupButton.isEnabled = true
                }
            }
        })

        viewModel.getIsSuccess().observe(this, Observer<Boolean> { isSuccess ->
            run {
                if (isSuccess) {
                    findNavController().navigate(SignUpFragmentDirections.actionSignupFragmentToLoginFragment())
                }
            }
        })

        viewModel.getErrorMessage().observe(this, Observer<String> { errorMessage ->
            run {
                if (errorMessage.isNotEmpty()) {
                    binding.emailText.error = "Email already in use"
                    binding.emailText.isErrorEnabled = true
                }
            }
        })


        return binding.root
    }
}
