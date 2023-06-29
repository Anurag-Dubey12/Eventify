package com.example.eventmatics.RoomDatabase.Dao

//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import com.example.eventmatics.RoomDatabase.Modal.Event
//
//@Dao
//interface EventDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//     fun insertEvent(event: Event)
//
//    @Update
//     fun eventUpdate(event: Event)
//
//    @Delete
//     fun EventDelete(event: Event)
//
//    @Query("SELECT * FROM events")
//    fun getAllEvents(): LiveData<List<Event>>
//
//
//}