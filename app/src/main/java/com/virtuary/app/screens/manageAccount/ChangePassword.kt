package com.virtuary.app.screens.manageAccount

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentChangePasswordBinding
import com.virtuary.app.util.hideKeyboard

class ChangePassword : Fragment() {

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChangePasswordBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_change_password, container, false
        )
        binding.changePasswordViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.invalidCurrentPassword.observe(this, Observer { invalid ->
            if (invalid) {
                binding.currentPasswordText.error = getString(R.string.error_invalid_password)
                binding.currentPasswordText.isErrorEnabled = true
            } else {
                binding.currentPasswordText.isErrorEnabled = false
            }
        })

        binding.currentPasswordEdit.doAfterTextChanged {
            binding.currentPasswordText.isErrorEnabled = false
        }

        viewModel.invalidNewPassword.observe(this, Observer { invalid ->
            if (invalid) {
                binding.newPasswordText.error = getString(R.string.error_password_short)
                binding.newPasswordText.isErrorEnabled = true
            } else {
                binding.newPasswordText.isErrorEnabled = false
            }
        })

        binding.newPasswordEdit.doAfterTextChanged {
            binding.newPasswordText.isErrorEnabled = false
        }

        binding.newPasswordEdit.setOnKeyListener { v, keyCode, event ->
            if (keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER
                && event.action == KeyEvent.ACTION_UP
            ) {
                v.clearFocus()
                viewModel.onClick()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        viewModel.inProgress.observe(this, Observer {
            if (it) {
                binding.changePasswordBtn.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                hideKeyboard()
            } else {
                binding.changePasswordBtn.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.isSuccess.observe(this, Observer {
            if (it) {
                findNavController().navigateUp()
                Toast.makeText(context, "Password change successful", Toast.LENGTH_LONG).show()
            }
        })

        return binding.root
    }
}
