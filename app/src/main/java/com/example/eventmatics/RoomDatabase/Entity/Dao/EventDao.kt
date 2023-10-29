package com.example.eventmatics.RoomDatabase.Entity.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eventmatics.RoomDatabase.Entity.EventEntity
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase

@Dao
interface EventDao {
    @Query("SELECT Event_Name FROM EVENTS WHERE Event_Name= :EventName ")
    fun isEventNameExists(EventName: String): Boolean

    @Insert
    fun InsertData(event:EventEntity):Long
}