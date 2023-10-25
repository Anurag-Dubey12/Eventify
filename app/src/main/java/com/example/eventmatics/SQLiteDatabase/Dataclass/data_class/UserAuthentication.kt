package com.example.eventmatics.SQLiteDatabase.Dataclass.data_class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserAuthentication(
    val id:Int,
    val userid:String,
    val email:String,
    val password:String
) : Parcelable
