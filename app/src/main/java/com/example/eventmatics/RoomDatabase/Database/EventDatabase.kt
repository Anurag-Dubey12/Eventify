//package com.example.eventmatics.RoomDatabase.Database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.eventmatics.RoomDatabase.Dao.EventDao
//import com.example.eventmatics.RoomDatabase.Modal.Event
//
//@Database(entities = [Event::class], version = 1, exportSchema = false)
//abstract class EventDatabase : RoomDatabase() {
//
//    abstract fun eventDao(): EventDao
//
//    companion object {
//        @Volatile
//        var INSTANCE: EventDatabase?=null
//        fun getDatabaseInstance(context: Context): EventDatabase {
//            var tempInstance= INSTANCE
//            if(tempInstance !=null){
//                return tempInstance
//            }
//            synchronized(this){
//                val roomDatabaseInstance=Room.databaseBuilder(
//                    context, EventDatabase::class.java,
//                    "events"
//                ).allowMainThreadQueries().build()
//                INSTANCE =roomDatabaseInstance
//                return roomDatabaseInstance
//            }
//        }
//    }
//}
