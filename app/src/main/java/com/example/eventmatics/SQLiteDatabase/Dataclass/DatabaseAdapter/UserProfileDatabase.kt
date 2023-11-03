package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserProfileDatabase(context: Context):SQLiteOpenHelper(context,"USER_PROFILE",null,1) {
    companion object {
        private const val COLUMN_ID = "id"
        private const val DATABASE_TABLE = "USER_Info"
        private const val Name="User_Name"
        private const val Profile="User_Profile"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $DATABASE_TABLE (" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +
                "$Name TEXT," +
                "$Profile BLOB" +
                ")"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE")
        onCreate(db)
    }

    fun insertUserProfile(userProfile:UserProfile):Long{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(Name,userProfile.name)
            put(Profile,userProfile.Image)
        }
        return db.insertWithOnConflict(DATABASE_TABLE,null,value,SQLiteDatabase.CONFLICT_REPLACE)
    }

    @SuppressLint("Range")
    fun getUserProfilebyID(id:Long):UserProfile?{
        val  db=readableDatabase
        val query="SELECT * FROM $DATABASE_TABLE WHERE $COLUMN_ID = $id"
        val cursor=db.rawQuery(query,null)
        return if(cursor.moveToFirst()){
            val id=cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val username=cursor.getString(cursor.getColumnIndex(Name))
            val userprofile=cursor.getBlob(cursor.getColumnIndex(Profile))
            UserProfile(id,username,userprofile)
        }else{
            cursor.close()
            null
        }
    }

    fun updateUserProfile(userProfile: UserProfile): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Name, userProfile.name)
            put(Profile, userProfile.Image)
        }
        return db.update(DATABASE_TABLE, values, "$COLUMN_ID = ?", arrayOf(userProfile.id.toString()))
    }
}