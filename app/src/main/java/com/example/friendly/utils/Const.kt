package com.example.friendly.utils

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object Const {
    const val CURRENT_USER = "USER_KEY"
    const val PREF = "MYPREF"
    const val FIREBASE_DATABASE_ADDRESS = "gs://friendly-1147b.appspot.com"
//    val DATABASE = FirebaseDatabase.getInstance("https://friendly-1147b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
//    val FIREBASE_AUTH = FirebaseAuth.getInstance()
//    val currentUser = FIREBASE_AUTH.currentUser!!.uid
//    val FIREBASE_STORAGE = FirebaseStorage.getInstance(FIREBASE_DATABASE_ADDRESS).getReference("UserData").storage.getReference(currentUser)
}