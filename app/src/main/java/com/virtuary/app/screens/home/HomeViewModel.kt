package com.virtuary.app.screens.home

import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    // TODO: for testing (delete later)
    val artifactsTitle: List<String>
            = listOf("Love Letter", "Baseball",
        "SchoolSchoolSchoolSchoolSchoolSchoolSchoolSchoolSchool", "Flag", "Movie Ticket",
        "Rabbit", "PillowSchoolSchoolSchoolSchoolSchoolSchool", "Bow", "Toy",
        "Computer", "Pen", "Present", "Box")

    // TODO: change to Chip data type
    val artifactsRelatedTo: List<String>
            = listOf("Mom", "Mom", "Mommmmmmmmmmmmmmmm", "Mom", "Mom", "Mom",
        "Broooooooooooooooooooooooooooo", "Bro", "Bro", "Bro",
        "Dad", "Dad", "Dad")

    val artifactsLocation: List<String>
            = listOf("Indonesia","Indonesia","Indonesia","Indonesia","Indonesia","Indonesia",
        "Indonesia","Indonesia","Indonesia","Indonesia","Indonesia","Indonesia","Indonesia")

}