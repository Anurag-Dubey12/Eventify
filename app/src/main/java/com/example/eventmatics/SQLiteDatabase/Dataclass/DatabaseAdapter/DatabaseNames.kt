package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameHolder

class DatabaseNames(val context: Context,val Databasename:String):SQLiteOpenHelper(context,Databasename,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val COLUMN_ID = "id"
        //Table and column
        private const val DATABASE_TABLE = "Database_Table"
        private const val DATABASE_NAME = "Database_Name"
        private const val DATABASE_DATE = "Database_Date"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $DATABASE_TABLE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$DATABASE_NAME TEXT," +
                "$DATABASE_DATE TEXT" + ")"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE")
        onCreate(db)
    }

    fun createDatabase(databaseNameHolder: DatabaseNameHolder): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DATABASE_NAME, databaseNameHolder.DatabaseName)
            put(DATABASE_DATE, databaseNameHolder.Date)
        }
        val id = db.insert(DATABASE_TABLE, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getAllEventNames(): List<DatabaseNameHolder> {
        val databaseNames = ArrayList<DatabaseNameHolder>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE"
        val cursor = db.rawQuery(query, null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(DATABASE_NAME))
                    val date = it.getString(it.getColumnIndex(DATABASE_DATE))
                    val databaseNameHolder = DatabaseNameHolder(id, name, date)
                    databaseNames.add(databaseNameHolder)
                } while (it.moveToNext())
            }
        }
        return databaseNames
    }

    fun deleteEventName(databaseNameHolder: DatabaseNameHolder): Int {
        val db = this.writableDatabase
        val query = db.delete(
            DATABASE_TABLE,
            "$COLUMN_ID = ? ",
            arrayOf(databaseNameHolder.id.toString())
        )
        db.close()
        return query
    }

    fun updateEvent(databaseNameHolder: DatabaseNameHolder): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DATABASE_NAME, databaseNameHolder.DatabaseName)
            put(DATABASE_DATE, databaseNameHolder.Date)
        }
        val rowsAffected = db.update(
            DATABASE_TABLE,
            values,
            "$COLUMN_ID = ?",
            arrayOf(databaseNameHolder.id.toString())
        )
        db.close()
        return rowsAffected
    }

}