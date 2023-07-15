package com.example.eventmatics.fragments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.DatabaseAdapter
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameHolder
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseSingleton
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseSingleton.databaseHelper
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class EventAdding(context: Context, private val fragmentManager: FragmentManager
) : AppCompatDialog(context) {
    private lateinit var eventName: TextInputEditText
    private lateinit var eventDate: TextInputEditText
    private lateinit var eventTime: TextInputEditText
    private lateinit var eventBudget: TextInputEditText
    private lateinit var createButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_event_adding)
        eventName = findViewById(R.id.eventname)!!
        eventDate = findViewById(R.id.eventdate)!!
        eventTime = findViewById(R.id.eventtime)!!
        eventBudget = findViewById(R.id.eventbudget)!!
        createButton = findViewById(R.id.eventcreatebut)!!
        val window = window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        eventDate.setOnClickListener { showDatePicker() }
        eventTime.setOnClickListener { showTimePicker() }
        createButton.setOnClickListener {
            val eventNameText = eventName.text.toString()
            val eventDateText = eventDate.text.toString()
            val eventTimeText = eventTime.text.toString()
            val eventBudgetText = eventBudget.text.toString()

            val event=Events(0,eventNameText,eventDateText,eventTimeText,eventBudgetText)
            val EventNameHolder=DatabaseNameHolder(0,eventNameText,eventDateText)
            val databaseHelper=LocalDatabase(context,eventNameText)
//            val EventDatabaseHelper=DatabaseAdapter(context,eventNameText)
            databaseHelper.createEvent(event)
//            EventDatabaseHelper.createDatabase(EventNameHolder)
            saveToSharedPreferences(context,"databasename",eventNameText)
            Toast.makeText(context, "Event created successfully", Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }
    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(minute)
            .setTitleText("Select Event Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, second)
            eventTime.setText(formattedTime)

        }

        timePicker.show(fragmentManager, "TAG_TIME_PICKER")
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDate
            val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val selectedMonth = selectedCalendar.get(Calendar.MONTH)
            val selectedYear = selectedCalendar.get(Calendar.YEAR)
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            eventDate.setText(formattedDate)
        }
        datePicker.show(fragmentManager, "datePicker")
    }

}