package com.example.friendly.utils

import com.google.firebase.database.FirebaseDatabase

object Const {
    val CURRENT_USER = "USER_KEY"
    val PREF = "MYPREF"
    val database = FirebaseDatabase.getInstance("https://friendly-1147b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
}