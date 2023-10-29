package com.example.eventmatics.RoomDatabase.Entity.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventmatics.RoomDatabase.Entity.Dao.EventDao
import com.example.eventmatics.RoomDatabase.Entity.EventEntity

@Database(entities = [EventEntity::class], version = 1)
abstract class Event_Database:RoomDatabase() {
    abstract fun eventdao():EventDao
    companion object{
        lateinit var databse:Event_Database
         fun getDatabase(context: Context,dbName:String)=
            Room.databaseBuilder(
                context.applicationContext,
                Event_Database::class.java,
                dbName
            ).build()
    }


}