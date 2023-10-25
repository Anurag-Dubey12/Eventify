package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.UserAuthentication

class AuthenticationDatabase(context: Context) :
    SQLiteOpenHelper(context, databaseName, null, 1) {

    companion object {
        const val databaseName = "SignLog"
        const val database_table="User_Details"
        private const val COLUMN_ID = "id"
        const val User_ID="User_ID"
        const val User_Email="Email"
        const val User_Password="Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query="CREATE TABLE $database_table ( "+
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$User_ID TEXT UNIQUE, "+
                "$User_Email TEXT UNIQUE,"+
                "$User_Password TEXT "+")"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $database_table")
    }
    fun insertData(user: UserAuthentication):Long{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(User_ID,user.userid )
            put(User_Email,user.email )
            put(User_Password, user.password)
        }
        val id=db.insert(database_table,null,value)
        db.close()
        return  id
    }
    @SuppressLint("Range")
    fun GetUserID(email:String):String{
        val db=readableDatabase
        var userid:String=" "
        val query="SELECT $User_ID FROM $database_table WHERE $User_Email = ?"
        val cursor=db.rawQuery(query, arrayOf(email))
        cursor?.let {
            if(cursor!=null){
                userid=it.getString(cursor.getColumnIndex(User_ID))
            }
        }
        cursor.close()
        db.close()
        return userid
    }

}
