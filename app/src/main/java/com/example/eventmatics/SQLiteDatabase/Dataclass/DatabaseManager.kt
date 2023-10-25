package com.example.eventmatics.SQLiteDatabase.Dataclass

import android.content.Context
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.getSharedPreference

object DatabaseManager {
    private var database: LocalDatabase? = null

    fun initialize(context: Context) {
        val databaseName = getSharedPreference(context, "databasename").toString()
        database = LocalDatabase(context, databaseName)
    }

    fun changeDatabaseName(context: Context, newDatabaseName: String) {
        saveToSharedPreferences(context, "databasename", newDatabaseName)
        if (database != null && database!!.databaseName != newDatabaseName) {
            database?.close()
            database = LocalDatabase(context, newDatabaseName)
        }
    }
    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun getDatabase(context: Context): LocalDatabase {
        val currentDatabaseName = getSharedPreference(context, "databasename").toString()
        if (database == null || database!!.databaseName != currentDatabaseName) {
            database = LocalDatabase(context, currentDatabaseName)
        }

        return database!!
    }
}
