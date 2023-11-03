package com.example.eventmatics.SQLiteDatabase.Dataclass.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Events(
    val id: Long,
    val name:String,
    val Date:String,
    val time:String,
    val budget:String,
    val userid:String?
) : Parcelable
