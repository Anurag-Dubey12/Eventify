//package com.example.eventmatics.RoomDatabase.ViewModel
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.viewModelScope
//import com.example.eventmatics.RoomDatabase.Repository.EventRepository
//import com.example.eventmatics.RoomDatabase.Database.EventDatabase
//import com.example.eventmatics.RoomDatabase.Modal.Event
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class EventViewModel(application: Application):AndroidViewModel(application) {
////    private val readalldata:LiveData<List<Event>>
//    private val repo: EventRepository
//    init {
//        val eventdao= EventDatabase.getDatabaseInstance(application).eventDao()
//        repo= EventRepository(eventdao)
//    }
//    fun addEvent(event: Event){
//        viewModelScope.launch(Dispatchers.IO){
//            repo.insertEvent(event)
//        }
//    }
//    fun getEvent():LiveData<List<Event>> = repo.getallevent()
//
//    fun updateEvent(event: Event){
//        repo.updateEvent(event)
//    }
//
//}