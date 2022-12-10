package com.example.navalbattlegameapplication.models

data class User(
    var fullname: String? = null,
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var active: Boolean? = false,
    var maxPoints: Number? = null,
    var currentPoints: Number? = null
)
