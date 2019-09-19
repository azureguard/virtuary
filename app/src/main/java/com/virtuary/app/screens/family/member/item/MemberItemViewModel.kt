package com.virtuary.app.screens.family.member.item

import androidx.lifecycle.ViewModel

class MemberItemViewModel : ViewModel() {
    //TODO: Change to livedata that's fetched from the database
    // probably need to change a bit of implementation in the fragment using observe or other method
    val artifactsTitle: List<String> = listOf(
        "Love Letter", "Baseball",
        "SchoolSchoolSchoolSchoolSchoolSchoolSchoolSchoolSchool", "Flag", "Movie Ticket",
        "Rabbit", "PillowSchoolSchoolSchoolSchoolSchoolSchool", "Bow", "Toy",
        "Computer", "Pen", "Present", "Box"
    )

    val artifactsRelatedTo: List<String> = listOf(
        "Mom", "Mom", "Mommmmmmmmmmmmmmmm", "Mom", "Mom", "Mom",
        "Broooooooooooooooooooooooooooo", "Bro", "Bro", "Bro",
        "Dad", "Dad", "Dad"
    )

    val artifactsLocation: List<String> = listOf(
        "Indonesia", "Indonesia", "Indonesia", "Indonesia", "Indonesia", "Indonesia",
        "Indonesia", "Indonesia", "Indonesia", "Indonesia", "Indonesia", "Indonesia", "Indonesia"
    )
}
