package com.example.eventmatics.Login_Activity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileInfo(
    val photo:Int,
    val name:String
) : Parcelable
