package com.example.eventmatics.SQLiteDatabase.Dataclass.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Budget(
    val id:Long,
    val name: String = "",
    val category: String = "",
    val note: String = "",
    val estimatedAmount: String,
    val balance: String,
    val remaining: String,
    var paid: String
) : Parcelable
