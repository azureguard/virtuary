package com.virtuary.app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    val name = MutableLiveData("")
    val imageUploaded = MutableLiveData<String>()
}
