package com.example.eventmatics.SQLiteDatabase.Dataclass

import android.content.Context
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.getSharedPreference

object DatabaseManager {
    private var database: LocalDatabase? = null

    fun initialize(context: Context) {
        // Initialize the database with the default user-defined database name
        val databaseName = getSharedPreference(context, "databasename").toString()
        database = LocalDatabase(context, databaseName)
    }

    fun changeDatabaseName(context: Context, newDatabaseName: String) {
        // Save the new database name to shared preferences
        saveToSharedPreferences(context, "databasename", newDatabaseName)
    }
    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun getDatabase(context: Context): LocalDatabase {
        // Check if the database is null or if the database name has changed
        val currentDatabaseName = getSharedPreference(context, "databasename").toString()
        if (database == null || database!!.databaseName != currentDatabaseName) {
            // Reinitialize the database with the new database name
            database = LocalDatabase(context, currentDatabaseName)
        }

        return database!!
    }
}
