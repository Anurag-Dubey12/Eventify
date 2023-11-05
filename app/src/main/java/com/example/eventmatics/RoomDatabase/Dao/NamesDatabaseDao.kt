package com.example.eventmatics.RoomDatabase.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventmatics.RoomDatabase.DataClas.DatabaseNameDataClass

@Dao
interface NamesDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertNames(names:DatabaseNameDataClass):Long

    @Query("SELECT * FROM Database_Table WHERE User_ID = :uid ")
    fun getAllEventNamesWithoutDuplicatesForUser(uid:String?): MutableList<DatabaseNameDataClass>

    @Delete
    fun deleteEventName(databaseNameDataClass: DatabaseNameDataClass): Int
}