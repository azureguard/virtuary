package com.virtuary.app.screens.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.virtuary.app.R

class EditPreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}
