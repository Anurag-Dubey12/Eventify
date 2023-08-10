package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameDataClass

class NamesDatabase(val context: Context):SQLiteOpenHelper(context,"Databasename_Names",null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val COLUMN_ID = "id"
        //Table and column
        private const val DATABASE_TABLE = "Database_Table"
        private const val DATABASE_NAME = "Database_Name"
        private const val DATABASE_DATE = "Database_Date"
        private const val DATABASE_Time = "Database_Time"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $DATABASE_TABLE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$DATABASE_NAME TEXT," +
                "$DATABASE_DATE TEXT,"+
                "$DATABASE_Time TEXT"+ ")"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE")
        onCreate(db)
    }

    fun createDatabase(databaseNameDataClass: DatabaseNameDataClass): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DATABASE_NAME, databaseNameDataClass.DatabaseName)
            put(DATABASE_DATE, databaseNameDataClass.Date)
            put(DATABASE_Time, databaseNameDataClass.Time)
        }
        val id = db.insert(DATABASE_TABLE, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getAllEventNames(): List<DatabaseNameDataClass> {
        val databaseNames = ArrayList<DatabaseNameDataClass>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE"
        val cursor:Cursor? = db.rawQuery(query, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(DATABASE_NAME))
                    val date = it.getString(it.getColumnIndex(DATABASE_DATE))
                    val time = it.getString(it.getColumnIndex(DATABASE_Time))
                    val databaseNameDataClass = DatabaseNameDataClass(id, name, date,time)
                    databaseNames.add(databaseNameDataClass)
                } while (it.moveToNext())
            }
        }
        return databaseNames
    }

    fun deleteEventName(databaseNameDataClass: DatabaseNameDataClass): Int {
        val db = this.writableDatabase
        val query = db.delete(
            DATABASE_TABLE,
            "$COLUMN_ID = ? ",
            arrayOf(databaseNameDataClass.id.toString())
        )
        db.close()
        return query
    }

    fun updateEvent(databaseNameDataClass: DatabaseNameDataClass): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DATABASE_NAME, databaseNameDataClass.DatabaseName)
            put(DATABASE_DATE, databaseNameDataClass.Date)
            put(DATABASE_Time, databaseNameDataClass.Time)
        }
        val rowsAffected = db.update(
            DATABASE_TABLE,
            values,
            "$COLUMN_ID = ?",
            arrayOf(databaseNameDataClass.id.toString())
        )
        db.close()
        return rowsAffected
    }

}