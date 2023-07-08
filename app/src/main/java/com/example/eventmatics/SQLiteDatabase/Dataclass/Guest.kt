package com.example.eventmatics.SQLiteDatabase.Dataclass

data class Guest(
    val id: Long,
    val name: String,
    val totalFamilyMembers: Int,
    val maleNumber: Int,
    val femaleNumber: Int,
    val note: String,
    val isInvitationSent: String,
    val phoneNumber: String,
    val email: String,
    val address: String
)
