package com.example.eventmatics.SQLiteDatabase.Dataclass

data class Vendor(
    val id:Long,
    val name: String = "",
    val category: String = "",
    val note: String = "",
    val estimatedAmount: String ,
    val balance: String ,
    val remaining: String,
    val paid: String ,
    val phonenumber:String,
    val emailid:String,
    val website:String
)
