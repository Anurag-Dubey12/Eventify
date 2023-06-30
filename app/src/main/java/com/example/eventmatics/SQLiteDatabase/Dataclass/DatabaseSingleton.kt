package com.example.eventmatics.SQLiteDatabase.Dataclass

import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase

object DatabaseSingleton {
    lateinit var databaseHelper: LocalDatabase
     var databaseName: String=" "
    fun initDatabaseName(databaseName: String) {
        this.databaseName = databaseName
    }
    fun retrieveDatabaseName(): String {
        return databaseName
    }
}