package com.virtuary.app.screens.manageAccount

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class EditProfileDetailsViewModel : ViewModel() {
    val name = ObservableField("Tester")
    val email = ObservableField("test@example.com")
}
