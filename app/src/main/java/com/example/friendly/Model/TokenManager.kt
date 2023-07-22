package com.example.friendly.Model

import android.content.Context
import android.util.Log
import com.example.friendly.utils.Const.CURRENT_USER
import com.example.friendly.utils.Const.PREF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TokenManager(context: Context) {

    val pref = context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
    val editor = pref.edit()

    fun getCurrentUser() : String? {
        var user  = pref.getString(CURRENT_USER, null)
        return user
    }

    fun saveCurrentUser(userId: String){
        editor.apply {
            putString(CURRENT_USER, userId)
            apply()
        }
    }
}