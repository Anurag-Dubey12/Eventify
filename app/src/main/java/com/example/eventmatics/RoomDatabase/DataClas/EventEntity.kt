package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Event")
@Parcelize
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "Event_Name")
    val name: String,
    @ColumnInfo(name = "Event_Date")
    val date: String,
    @ColumnInfo(name = "Event_Time")
    val time: String,
    @ColumnInfo(name = "Event_Budget")
    val budget: String,
    @ColumnInfo(name = "UserID")
    val userid: String?
): Parcelable
