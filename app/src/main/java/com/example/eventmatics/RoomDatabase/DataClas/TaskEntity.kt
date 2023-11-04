package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Task")
@Parcelize
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Long = 0,
    @ColumnInfo(name = "Task_Name")
    val taskName: String,
    @ColumnInfo(name = "Task_Category")
    val category: String,
    @ColumnInfo(name = "Task_Note")
    val taskNote: String,
    @ColumnInfo(name = "Task_Status")
    val taskStatus: String,
    @ColumnInfo(name = "Task_Date")
    val taskDate: String
) : Parcelable
