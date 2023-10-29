package com.example.eventmatics.RoomDatabase.Entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    val id: Long = 0,

    @ColumnInfo(name = "Event_Name") // Specify the column name
    var name: String,

    @ColumnInfo(name = "Event_Date") // Specify the column name
    val date: String,

    @ColumnInfo(name = "Event_Time") // Specify the column name
    val time: String,

    @ColumnInfo(name = "Event_Budget") // Specify the column name
    val budget: String,

    @ColumnInfo(name = "UserID") // Specify the column name
    val userId: String?
) : Parcelable

