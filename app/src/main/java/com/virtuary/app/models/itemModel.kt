package com.virtuary.app.models

import java.util.Date

data class Item(
    val name: String? = null,
    val originalLocation: String? = null,
    val currentLocation: String? = null,
    val whenExist: Date? = null,
    val story: String? = null,
    val relations: List<String>? = null,
    val pictures: List<String>? = null
)