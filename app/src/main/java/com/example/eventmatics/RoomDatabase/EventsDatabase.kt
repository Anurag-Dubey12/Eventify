package com.example.eventmatics.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventmatics.RoomDatabase.Dao.EventsDao
import com.example.eventmatics.RoomDatabase.DataClas.BudgetEntity
import com.example.eventmatics.RoomDatabase.DataClas.EventEntity
import com.example.eventmatics.RoomDatabase.DataClas.GuestEntity
import com.example.eventmatics.RoomDatabase.DataClas.PaymentEntity
import com.example.eventmatics.RoomDatabase.DataClas.TaskEntity
import com.example.eventmatics.RoomDatabase.DataClas.VendorEntity
import com.example.eventmatics.RoomDatabase.DataClas.VendorPaymentEntity

@Database(entities = [EventEntity::class, TaskEntity::class, BudgetEntity::class,PaymentEntity::class,
                    VendorPaymentEntity::class,GuestEntity::class,VendorEntity::class], version = 1)
abstract class EventsDatabase:RoomDatabase() {
    abstract fun eventdao():EventsDao

    companion object{
        lateinit var database: EventsDatabase

        fun createDatabase(context: Context,dbname:String)=
            Room.databaseBuilder(
                context.applicationContext,
                EventsDatabase::class.java,
                dbname
            ).build()
    }
}