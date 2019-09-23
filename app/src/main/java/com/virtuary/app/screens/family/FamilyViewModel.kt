package com.virtuary.app.screens.family

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FamilyViewModel
    : ViewModel() {

    private val _users = MutableLiveData<MutableList<User>>()
    val users: LiveData<MutableList<User>>
        get() = _users

    // TODO: delete later
    private val familyMemberName: List<String> = listOf(
        "Hello World Kevin Steve Daryl Huston Kevin !!!!!!!!!!!!!!!!!!!!",
        "Hello World Steve",
        "Hello World Daryl",
        "Hello World Huston",
        "Hello World Bob",
        "Hello World Lev",
        "Hello World Kevin !!!!!!!",
        "Hello World Steve!!!!!!",
        "Hello World Daryl",
        "Hello World Huston",
        "Hello World Bob",
        "Hello World Lev"
    )

    private val familyMemberEmail: List<String> =
        listOf(
            "abc@gmail.com",
            "abd@gmail.com",
            "abe@gmail.com",
            "abf@gmail.com",
            "abg@gmail.com",
            "abf@gmail.com",
            "abcde@gmail.com",
            "abc@gmail.com",
            "abc@gmail.com",
            "abc@gmail.com",
            "abc@gmail.com",
            "abc@gmail.com"
        )

    init {
        addUsers()
    }

    private fun addUsers() {
        for (index in familyMemberName.indices) {
            _users.value!!.add(
                User(
                    familyMemberName[index],
                    familyMemberEmail[index]
                    )
            )
        }
    }
}
