package com.virtuary.app.screens.landing

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.virtuary.app.R
import com.virtuary.app.databinding.FragmentLandingBinding




/**
 * Fragment for the starting screen of the app
 */
class LandingFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentLandingBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_landing, container, false
        )

        val nightModeFlags =
            context!!.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> binding.virtuaryLogo.setImageResource(R.drawable.ic_virtuary_dark_01)
        }

        // Hide activity bar in the first fragment
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Listener for button for navigation
        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToSignupFragment())
            (activity as AppCompatActivity).supportActionBar?.show()
        }

        binding.logInButton.setOnClickListener {
            findNavController().navigate(LandingFragmentDirections.actionLandingFragmentToLoginFragment())
            (activity as AppCompatActivity).supportActionBar?.show()
        }

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_global_homeFragment)
            (activity as AppCompatActivity).supportActionBar?.show()
        }

        return binding.root
    }
}
