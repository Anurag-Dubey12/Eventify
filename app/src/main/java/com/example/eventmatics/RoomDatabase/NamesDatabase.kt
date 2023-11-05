package com.example.eventmatics.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventmatics.RoomDatabase.Dao.NamesDatabaseDao
import com.example.eventmatics.RoomDatabase.DataClas.DatabaseNameDataClass

@Database(entities = [DatabaseNameDataClass::class], version = 1)
abstract class NamesDatabase:RoomDatabase() {
    abstract fun namesdatabasedao():NamesDatabaseDao
    companion object{
        lateinit var database: EventsDatabase

        fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                NamesDatabase::class.java,
                "Database_Table"
            ).build()
    }
}