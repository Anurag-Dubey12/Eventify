package com.example.eventmatics.SQLiteDatabase.Dataclass

data class Guest(
    val id: Long,           // Primary Key
    val name: String,
    val gender: String,
    val type: String,
    val note: String,
    val status: String,
    val contact: String,
    val email: String,
    val address: String
)
