package com.example.eventmatics.SQLiteDatabase.Dataclass.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Guest(
    val id: Long,
    val name: String,
    val totalFamilyMembers: String,
    val note: String,
    val isInvitationSent: String,
    val phoneNumber: String,
    val Acceptence:String,
    val address: String
) : Parcelable
