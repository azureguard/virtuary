package com.virtuary.app.screens.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentLoginBinding
import com.virtuary.app.util.hideKeyboard
import com.virtuary.app.util.showKeyboard


/**
 * Fragment for the Login screen of the app
 */
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
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
                binding.emailEdit.error = "Invalid email address"
                binding.emailEdit.isErrorEnabled = true
                binding.loginEmailEdit.requestFocus()
            } else {
                binding.emailEdit.isErrorEnabled = false
            }
        })
        binding.loginEmailEdit.doAfterTextChanged {
            run {
                binding.emailEdit.isErrorEnabled = false
            }
        }

        // Sets up event listening to show error when the password is invalid
        viewModel.invalidPassword.observe(this, Observer { invalid ->
            if (invalid) {
                binding.passwordEdit.error = "Password cannot be blank"
                binding.passwordEdit.isErrorEnabled = true
            } else {
                binding.passwordEdit.isErrorEnabled = false
            }
        })

        binding.loginPasswordEdit.doAfterTextChanged {
            run {
                binding.passwordEdit.isErrorEnabled = false
            }
        }

        binding.loginPasswordEdit.setOnKeyListener { v, keyCode, event ->
            run {
                if (keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_UP
                ) {
                    v.clearFocus()
                    viewModel.onClick()
                    return@run true
                }
                return@run false
            }
        }

        viewModel.getInProgress().observe(this, Observer<Boolean> { inProgress ->
            run {
                if (inProgress) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginButton.isEnabled = false
                    hideKeyboard()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.isEnabled = true
                }
            }
        })

        viewModel.getIsSuccess().observe(this, Observer<Boolean> { isSuccess ->
            run {
                if (isSuccess) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
            }
        })

        viewModel.getErrorMessage().observe(this, Observer<String> { errorMessage ->
            run {
                if (errorMessage.isNotEmpty()) {
                    binding.passwordEdit.error = "Email or password incorrect"
                    binding.passwordEdit.isErrorEnabled = true
//                    Snackbar.make(view!!, errorMessage, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        return binding.root
    }
}
