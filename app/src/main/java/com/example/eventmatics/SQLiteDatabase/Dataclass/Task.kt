package com.example.eventmatics.SQLiteDatabase.Dataclass

data class Task(
    val id: Long,
    val taskName: String,
    val category:String,
    val taskNote: String,
    val taskStatus: String,
    val taskDate: String,
)
