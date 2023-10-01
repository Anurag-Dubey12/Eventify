package com.example.eventmatics.SQLiteDatabase.Dataclass.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Long,
    val taskName: String,
    val category:String,
    val taskNote: String,
    val taskStatus: String,
    val taskDate: String,
) : Parcelable
