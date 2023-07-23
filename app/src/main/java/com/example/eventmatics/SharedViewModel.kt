package com.example.eventmatics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task

class SharedViewModel : ViewModel() {
    val selectedTask = MutableLiveData<Task>()
}