package com.virtuary.app.screens.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    // TODO: for testing (delete later)
    val artifactsTitle: List<String> = listOf(
        "Love Letter", "Baseball",
        "SchoolSchoolSchoolSchoolSchoolSchoolSchoolSchoolSchool", "Flag", "Movie Ticket"
    )

    // TODO: assign member list who related to each artifact here
    private var artifactsMemberList: ArrayList<List<String>> = arrayListOf()
    val artifactsRelatedTo: ArrayList<String> = arrayListOf()

    val artifactsLocation: List<String> = listOf(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry", "Indonesia",
        "Indonesia", "Indonesia", "Indonesia"
    )

    init {
        addMemberList()
        convertListToText()
    }

    private fun addMemberList() {
        artifactsMemberList.add(listOf("Mom", "Dad"))
        artifactsMemberList.add(listOf("Brother", "Sister", "Mom", "Dad", "Grandmother"))
        artifactsMemberList.add(listOf("Dad", "Brother", "Mom", "Sister", "Grandmother"))
        artifactsMemberList.add(listOf("Grandmother", "Mom", "Dad", "Brother", "Sister"))
        artifactsMemberList.add(listOf("Sister", "Grandmother", "Mom", "Dad", "Brother"))
    }

    private fun convertListToText() {
        for (listOfMember in artifactsMemberList) {
            var result = ""
            for (member in listOfMember) {
                result += member

                // put "," after member iff it is not the last member
                if (listOfMember.indexOf(member) != (listOfMember.size - 1)) {
                    result += ", "
                }
            }
            artifactsRelatedTo.add(result)
        }
    }
}
