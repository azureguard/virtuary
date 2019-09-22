package com.virtuary.app.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _artifacts = MutableLiveData<MutableList<Artifact>>()
    val artifacts: LiveData<MutableList<Artifact>>
        get() = _artifacts

    // TODO: for testing (delete later)
    private val artifactsTitle: List<String> = listOf(
        "Love Letter", "Baseball",
        "SchoolSchoolSchoolSchoolSchoolSchoolSchoolSchoolSchool", "Flag", "Movie Ticket"
    )

    // TODO: assign member list who related to each artifact here
    private var artifactsMemberList: MutableList<List<String>> = mutableListOf()
    private val artifactsRelatedTo: MutableList<String> = mutableListOf()

    val artifactsLocation: List<String> = listOf(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry", "Indonesia",
        "Indonesia", "Indonesia", "Indonesia"
    )

    init {
        addMemberList()
        convertListToText()
        _artifacts.value = mutableListOf()
        addArtifacts()
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

    private fun addArtifacts() {
        for (index in artifactsTitle.indices) {
            _artifacts.value!!.add(
                Artifact(
                    artifactsTitle[index],
                    artifactsRelatedTo[index],
                    artifactsLocation[index]
                )
            )
        }
    }
}
