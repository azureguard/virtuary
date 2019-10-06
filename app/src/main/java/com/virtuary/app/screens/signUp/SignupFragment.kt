package com.virtuary.app.screens.signUp

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentSignUpBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container, false
        )

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
                binding.emailText.error = getString(R.string.error_invalid_email)
                binding.emailText.isErrorEnabled = true
            } else {
                binding.emailText.isErrorEnabled = false
            }
        })
        binding.emailEdit.doAfterTextChanged {
            run {
                binding.emailText.isErrorEnabled = false
            }
        }

        // Sets up event listening to show error when the password is invalid
        viewModel.invalidPassword.observe(this, Observer { invalid ->
            if (invalid) {
                binding.passwordText.error = getString(R.string.error_password_short)
                binding.passwordText.isErrorEnabled = true
            } else {
                binding.passwordText.isErrorEnabled = false
            }
        })
        binding.passwordEdit.doAfterTextChanged {
            run {
                binding.passwordText.isErrorEnabled = false
            }
        }

        binding.passwordEdit.setOnKeyListener { v, keyCode, event ->
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

        // Sets up event listening to show error when the name is invalid
        viewModel.invalidName.observe(this, Observer { invalid ->
            if (invalid) {
                binding.nameText.error = getString(R.string.error_name_blank)
                binding.nameText.isErrorEnabled = true
            } else {
                binding.nameText.isErrorEnabled = false
            }
        })

        binding.nameEdit.doAfterTextChanged {
            run {
                binding.nameText.isErrorEnabled = false
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
                    findNavController().navigate(SignUpFragmentDirections.actionGlobalHomeFragment())
                }
            }
        })

        viewModel.getErrorMessage().observe(this, Observer<Int> { errorMessage ->
            if (errorMessage != R.string.error_server_unreachable || errorMessage != R.string.error_user_profile_not_set) {
                binding.emailText.error = getString(R.string.error_email_in_use)
                binding.emailText.isErrorEnabled = true
            }
        })


        return binding.root
    }
}
