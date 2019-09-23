package com.virtuary.app.screens.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentLandingBinding


/**
 * Fragment for the starting screen of the app
 */
class LandingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentLandingBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_landing, container, false)

        // Hide activity bar in the first fragment
        (activity as AppCompatActivity).supportActionBar!!.hide()

        // Listener for button for navigation
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToSignupFragment())
            (activity as AppCompatActivity).supportActionBar!!.show()
        }

        binding.logInButton.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToLoginFragment())
            (activity as AppCompatActivity).supportActionBar!!.show()
        }

        // TODO: Change the navigation & the navigation graph (for now automatically go to home)
//        findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToHomeFragment())
        (activity as AppCompatActivity).supportActionBar!!.show()

        return binding.root
    }
}
