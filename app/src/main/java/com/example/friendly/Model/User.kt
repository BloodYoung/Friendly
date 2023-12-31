package com.example.friendly.Model

import android.net.Uri

data class User(
    val firstName: String ?= null,
    val lastName : String ?= null,
    val email: String ?= null,
    val gender : String ?= null,
    val age: Int ?=null,
    val profilePicture : Uri ?=null
)
