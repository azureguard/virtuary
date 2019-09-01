package com.virtuary.app.screens.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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

        (activity as AppCompatActivity).supportActionBar!!.hide()

        return binding.root
    }
}
