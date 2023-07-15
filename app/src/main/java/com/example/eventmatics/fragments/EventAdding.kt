package com.example.eventmatics.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.FragmentManager
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseNameHolder
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class EventAdding(
    context: Context, private val fragmentManager: FragmentManager,
    private val eventId: Int?
) : AppCompatDialog(context) {
    private lateinit var eventName: TextInputEditText
    private lateinit var eventDate: TextInputEditText
    private lateinit var eventTime: TextInputEditText
    private lateinit var eventBudget: TextInputEditText
    private lateinit var createButton: Button
    private lateinit var EditButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_event_adding)
        eventName = findViewById(R.id.eventname)!!
        eventDate = findViewById(R.id.eventdate)!!
        eventTime = findViewById(R.id.eventtime)!!
        eventBudget = findViewById(R.id.eventbudget)!!
        createButton = findViewById(R.id.eventcreatebut)!!
        EditButton = findViewById(R.id.eventeditbut)!!
        val window = window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        if (eventId != null) {
            // Retrieve data from SQLite using the eventId for editing
            val eventData = getEventDataFromSQLite(eventId)
            eventName.setText(eventData.name)
//            val newName=eventName.setText(eventData.name)
            eventDate.setText(eventData.Date)
            eventTime.setText(eventData.time)
            eventBudget.setText(eventData.budget)
            createButton.visibility= View.GONE
            EditButton.visibility=View.VISIBLE
            EditButton.setOnClickListener {
                val databasename=getSharedPreference(context,"databasename").toString()
                val event=Events(0,eventName.toString(),eventDate.toString(),eventTime.toString(),eventBudget.toString())
                val databaseHelper=LocalDatabase(context,databasename)
                databaseHelper.updateEvent(event)
                Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show()
                saveToSharedPreferences(context,"databasename",eventName.toString())
                dismiss()
            }
        }
        eventDate.setOnClickListener { showDatePicker() }
        eventTime.setOnClickListener { showTimePicker() }
        createButton.setOnClickListener {
            val eventNameText = eventName.text.toString()
            val eventDateText = eventDate.text.toString()
            val eventTimeText = eventTime.text.toString()
            val eventBudgetText = eventBudget.text.toString()
            val event=Events(0,eventNameText,eventDateText,eventTimeText,eventBudgetText)
            val databaseHelper=LocalDatabase(context,eventNameText)

//            if (eventId != null) {
//                // Updating an existing event
//                databaseHelper.updateEvent(event)
//                Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show()
//                saveToSharedPreferences(context,"databasename",eventNameText)
//
//            }
//            else{
            databaseHelper.createEvent(event)
            Toast.makeText(context, "Event created successfully", Toast.LENGTH_SHORT).show()
            saveToSharedPreferences(context,"databasename",eventNameText)
//            }


            dismiss()
        }

    }
    private fun getEventDataFromSQLite(position: Int): Events {
        val databasename=getSharedPreference(context,"databasename").toString()
        val databaseHelper = LocalDatabase(context, databasename)
        val eventDataList = databaseHelper.getAllEvents()
        return eventDataList[position]
    }

    fun saveToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
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