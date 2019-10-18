package com.virtuary.app.firebase

data class User(
    var name: String? = null,
    var image: String? = null,
    var alias: Map<String, String>? = null,
    var item: List<Item>? = null
)
