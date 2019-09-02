package com.virtuary.app.screens.home

import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    // TODO: for testing (delete later)
    val artifactsTitle: List<String>
            = listOf("Love Letter", "Baseball",
        "School", "Flag", "Movie Ticket",
        "Rabbit", "Pillow", "Bow", "Toy",
        "Computer", "Pen", "Present", "Box")
    val artifactsRelatedTo: List<String>
            = listOf("Mom", "Mom", "Mom", "Mom", "Mom", "Mom", "Bro", "Bro", "Bro", "Bro",
        "Dad", "Dad", "Dad")


}