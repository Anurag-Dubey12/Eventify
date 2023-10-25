package com.example.eventmatics.SQLiteDatabase.Dataclass

import android.content.Context
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.getSharedPreference

object AuthenticationUid {
    private const val USER_UID_KEY = "useruid"

    fun saveUserUid(context: Context, uid: String) {
        val sp = context.getSharedPreferences("UserUid", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(USER_UID_KEY, uid)
        editor.apply()
    }

    fun getUserUid(context: Context): String? {
        val sp = context.getSharedPreferences("UserUid", Context.MODE_PRIVATE)
        return sp.getString(USER_UID_KEY, null)
    }

    fun changeUserUid(context: Context, newUid: String) {
        val sp = context.getSharedPreferences("UserUid", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(USER_UID_KEY, newUid)
        editor.apply()
    }
}