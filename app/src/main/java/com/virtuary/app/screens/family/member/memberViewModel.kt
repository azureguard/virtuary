package com.virtuary.app.screens.family.member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemberViewModel : ViewModel() {

    // Live Data To be observed for conditional layout rendering
    private val _itemSize = MutableLiveData<Int>()
    val itemSize: LiveData<Int>
        get() = _itemSize

    // TODO: Change it with actual data from database using live data
    val artifactsTitle: List<String> = listOf(
        "Love Letter", "Baseball",
        "SchoolSchoolSchoolSchoolSchoolSchoolSchoolSchoolSchool", "Flag", "Movie Ticket",
        "Rabbit", "PillowSchoolSchoolSchoolSchoolSchoolSchool", "Bow", "Toy",
        "Computer", "Pen", "Present", "Box"
    )
}
