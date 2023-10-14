package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.DatabaseNameDataClass

class NamesDatabase(val context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val COLUMN_ID = "id"
        //Table and column
        private const val DATABASE_TABLE = "Database_Table"
        private const val DATABASE_NAME = "Database_Name"
        private const val DATABASE_DATE = "Database_Date"
        private const val DATABASE_Time = "Database_Time"
        private const val DATABASE_Budget = "Database_Budget"
        private const val UserID = "User_ID"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $DATABASE_TABLE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$DATABASE_NAME TEXT," +
                "$DATABASE_DATE TEXT,"+
                "$DATABASE_Time TEXT,"+
                "$DATABASE_Budget TEXT,"+
                "$UserID TEXT"+
                ")"
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
            put(DATABASE_Budget, databaseNameDataClass.Budget)
            put(UserID, databaseNameDataClass.uid)
        }
        val id = db.insertWithOnConflict(DATABASE_TABLE, null, values,SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return id
    }
    fun createOrUpdateDatabase(databaseNameDataClass: DatabaseNameDataClass): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(DATABASE_NAME, databaseNameDataClass.DatabaseName)
            put(DATABASE_DATE, databaseNameDataClass.Date)
            put(DATABASE_Time, databaseNameDataClass.Time)
            put(DATABASE_Budget, databaseNameDataClass.Budget)
            put(UserID, databaseNameDataClass.uid)
        }

        val existingEvent = getEventByName(databaseNameDataClass.DatabaseName)

        val id: Long
        if (existingEvent == null) {
            id = db.insertWithOnConflict(DATABASE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        } else {
            // Event name already exists, update the existing record
            id = db.update(
                DATABASE_TABLE,
                values,
                "$COLUMN_ID = ?",
                arrayOf(existingEvent.id.toString())
            ).toLong()
        }

        return id
    }

    @SuppressLint("Range")
    fun getEventByName(eventName: String): DatabaseNameDataClass? {
        val db = readableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE WHERE $DATABASE_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(eventName))
        var event: DatabaseNameDataClass? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(DATABASE_NAME))
            val date = cursor.getString(cursor.getColumnIndex(DATABASE_DATE))
            val time = cursor.getString(cursor.getColumnIndex(DATABASE_Time))
            val uid = cursor.getString(cursor.getColumnIndex(UserID))
            val budget = cursor.getString(cursor.getColumnIndex(DATABASE_Budget))
            event = DatabaseNameDataClass(id, name, date, time,budget,uid)
        }
        cursor?.close()
        db.close()
        return event
    }

    @SuppressLint("Range")
    fun GetNamesEventsData(eventid:Int): DatabaseNameDataClass?{
        val db=readableDatabase
        val query="SELECT * FROM $DATABASE_TABLE WHERE $COLUMN_ID = $eventid"
        val cursor=db.rawQuery(query,null)
        var names: DatabaseNameDataClass?=null
        if(cursor.moveToFirst()){
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(DATABASE_NAME))
            val date = cursor.getString(cursor.getColumnIndex(DATABASE_DATE))
            val time = cursor.getString(cursor.getColumnIndex(DATABASE_Time))
            val uid = cursor.getString(cursor.getColumnIndex(UserID))
            val budget = cursor.getString(cursor.getColumnIndex(DATABASE_Budget))
            names= DatabaseNameDataClass(id,name,date,time,budget,uid)
        }
        cursor?.close()
        db.close()
        return names
    }
    @SuppressLint("Range")
    fun getAllEventNames(): MutableList<DatabaseNameDataClass> {
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
                    val budget=it.getString(it.getColumnIndex(DATABASE_Budget))
                    val uid = cursor.getString(cursor.getColumnIndex(UserID))
                    val databaseNameDataClass = DatabaseNameDataClass(id, name, date,time,budget,uid)
                    databaseNames.add(databaseNameDataClass)
                } while (it.moveToNext())
            }
        }
        return databaseNames
    }
//    fun getAllEventNamesWithoutDuplicates(): MutableList<DatabaseNameDataClass> {
//        val databaseNames = ArrayList<DatabaseNameDataClass>()
//        val db = this.readableDatabase
//        val query = "SELECT * FROM $DATABASE_TABLE"
//        val cursor: Cursor? = db.rawQuery(query, null)
//        val uniqueEventNames = HashSet<String>()
//
//        cursor?.let {
//            if (it.moveToFirst()) {
//                do {
//                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
//                    val name = it.getString(it.getColumnIndex(DATABASE_NAME))
//                    val date = it.getString(it.getColumnIndex(DATABASE_DATE))
//                    val time = it.getString(it.getColumnIndex(DATABASE_Time))
//                    val uid = cursor.getString(cursor.getColumnIndex(UserID))
//
//                    // Check if the event name is unique
//                    if (uniqueEventNames.add(name)) {
//                        val databaseNameDataClass = DatabaseNameDataClass(id, name, date, time,uid)
//                        databaseNames.add(databaseNameDataClass)
//                    }
//                } while (it.moveToNext())
//            }
//        }
//
//        cursor?.close()
//        db.close()
//
//        return databaseNames
//    }
@SuppressLint("Range")
fun getAllEventNamesWithoutDuplicatesForUser(uid: String?): MutableList<DatabaseNameDataClass> {
        val databaseNames = ArrayList<DatabaseNameDataClass>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $DATABASE_TABLE WHERE $UserID = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(uid))
        val uniqueEventNames = HashSet<String>()
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(DATABASE_NAME))
                    val date = it.getString(it.getColumnIndex(DATABASE_DATE))
                    val time = it.getString(it.getColumnIndex(DATABASE_Time))
                    val budget=it.getString(it.getColumnIndex(DATABASE_Budget))
                    val uid = cursor.getString(cursor.getColumnIndex(UserID))
                    // Check if the event name is unique
                    if (uniqueEventNames.add(name)) {
                        val databaseNameDataClass = DatabaseNameDataClass(id, name, date, time,budget,uid)
                        databaseNames.add(databaseNameDataClass)
                    }
                } while (it.moveToNext())
            }
        }

        cursor?.close()
        db.close()

        return databaseNames
    }
    fun isEventNameExists(uid: String?, eventName: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $DATABASE_TABLE WHERE $UserID = ? AND $DATABASE_NAME = ?"
        val cursor: Cursor? = db.rawQuery(query, arrayOf(uid, eventName))
        var count = 0

        cursor?.let {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }

        cursor?.close()
        db.close()

        return count > 0
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
    fun deleteAll():Int{
        val db=writableDatabase
        val rowsaffected=db.delete(DATABASE_TABLE,null,null)
        db.close()
        return rowsaffected
    }

}