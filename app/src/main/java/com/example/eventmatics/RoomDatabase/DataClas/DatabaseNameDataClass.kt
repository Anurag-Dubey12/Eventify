package com.example.eventmatics.RoomDatabase.DataClas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Database_Table")
data class DatabaseNameDataClass(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id" )
    val id:Long=0,
    @ColumnInfo(name ="Database_Name" )
    val DatabaseName:String,
    @ColumnInfo(name ="Database_Date" )
    val Date:String="",
    @ColumnInfo(name = "Database_Time")
    val Time:String="",
    @ColumnInfo(name ="Database_Budget" )
    val Budget:String="",
    @ColumnInfo(name = "User_ID")
    val uid:String?
)