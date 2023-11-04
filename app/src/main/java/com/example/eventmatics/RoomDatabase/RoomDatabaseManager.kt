package com.example.eventmatics.RoomDatabase

import android.content.Context
import com.example.eventmatics.RoomDatabase.Dao.EventsDao
import com.example.eventmatics.getSharedPreference

object RoomDatabaseManager {
    private var database: EventsDatabase? = null

    fun initialize(context: Context) {
        val databaseName = getSharedPreference(context, "databasename").toString()
        database = EventsDatabase.createDatabase(context, databaseName)
    }

    fun changeDatabaseName(context: Context, newDatabaseName: String) {
        saveToSharedPreferences(context, "databasename", newDatabaseName)

        if (database != null) {
            database?.close()
        }

        database = EventsDatabase.createDatabase(context, newDatabaseName)
    }

    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getDatabase(context: Context): EventsDatabase {
        val currentDatabaseName = getSharedPreference(context, "databasename").toString()
        if (database == null || database!!.isOpen) {
            database = EventsDatabase.createDatabase(context, currentDatabaseName)
        }

        return database!!
    }
    fun getEventsDao(context: Context): EventsDao {
        return getDatabase(context).eventdao()
    }

}