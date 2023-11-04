package com.example.eventmatics.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.eventmatics.Notification
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.Dao.EventsDao
import com.example.eventmatics.RoomDatabase.DataClas.EventEntity
import com.example.eventmatics.RoomDatabase.EventsDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.AuthenticationUid
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Events
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.DatabaseNameDataClass
import com.example.eventmatics.messageExtra
import com.example.eventmatics.notificationID
import com.example.eventmatics.titleExtra
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EventAdding(
    context: Context, private val fragmentManager: FragmentManager,
    private val eventId: Int?
) : BottomSheetDialog(context){
    private lateinit var eventName: TextInputEditText
    private lateinit var eventDate: TextInputEditText
    private lateinit var eventTime: TextInputEditText
    private lateinit var eventBudget: TextInputEditText
    private lateinit var createButton: Button
    private lateinit var EditButton: Button

    private var db:EventsDatabase?=null
    private var eventdao:EventsDao?=null

    interface OnDataEnter{
        fun onDataEnter(event: EventEntity)
    }
    private var onDataEnterListener: OnDataEnter? = null
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
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

        val today = Calendar.getInstance()
        val todayFormattedDate = "${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.YEAR)}"
        val todayFormattedTime = String.format(
            "%02d:%02d:%02d",
            today.get(Calendar.HOUR_OF_DAY),
            today.get(Calendar.MINUTE),
            today.get(Calendar.SECOND)
        )
        eventDate.setText(todayFormattedDate)
        eventTime.setText(todayFormattedTime)
        eventDate.setOnClickListener { showDatePicker() }
        eventTime.setOnClickListener { showTimePicker() }

        createButton.setOnClickListener {
            val eventNameText = eventName.text.toString()
            var eventDateText = eventDate.text.toString()
            val eventTimeText = eventTime.text.toString()
            val eventBudgetText = eventBudget.text.toString()

            if (eventNameText.isEmpty()) {
                eventName.error = "Enter Event Name"
                return@setOnClickListener
            }
            if (eventDateText.isEmpty()) {
                eventDateText = "Date is not Defined"
            }
            if (eventBudgetText.isEmpty()) {
                eventBudget.error = "Enter Budget"
                return@setOnClickListener
            }
            if (eventTimeText.isEmpty()) {
                eventTime.error = "Select Time"
                return@setOnClickListener
            }
            val Databasename=NamesDatabase(context)
            try{
            db= EventsDatabase.createDatabase(context,eventNameText)
            eventdao=db?.eventdao()

                GlobalScope.launch(Dispatchers.IO) {
                    if (eventdao!!.isEventNameExists(eventNameText)) {
                        withContext(Dispatchers.Main) {
                            val rootView = findViewById<View>(android.R.id.content)
                            val snackbar = Snackbar.make(rootView!!, "Event name must be unique", Snackbar.LENGTH_SHORT)
                            val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
                            params.gravity = Gravity.TOP
                            snackbar.view.layoutParams = params
                            snackbar.show()
                        }
                        return@launch
                    }

            val uid=AuthenticationUid.getUserUid(context)!!
            val eventEntity=EventEntity(0,eventNameText,eventDateText,eventTimeText,eventBudgetText,uid)
            val names= DatabaseNameDataClass(0,eventNameText,eventDateText,eventTimeText, eventBudgetText,uid)
            val eventId = eventdao!!.InserEvent(eventEntity)
            Databasename.createDatabase(names)
            if (eventId!= -1L ) {
                val dataAddedIntent = Intent("com.example.eventmatics.fragments")
                context?.sendBroadcast(dataAddedIntent)
                onDataEnterListener?.onDataEnter(eventEntity)
                shownotification(eventNameText,eventDateText,eventTimeText)

                saveToSharedPreferences(context, "databasename", eventNameText)
                dismiss()
            }
            Databasename.close()
            }
            }catch (e:Exception){
                Log.d("eventCreation","Failed to create event:${e.message}")
            }
        }
    }

    private fun shownotification(name:String,Date:String,Time:String) {
            val title = "Event Started"
            val message = "Congrats !Your Event $name has been schedule on $Date at $Time "
            val notificationIntent = Intent(context, Notification::class.java)
            notificationIntent.putExtra(titleExtra, title)
            notificationIntent.putExtra(messageExtra, message)

            val pendingIntent = PendingIntent.getBroadcast(context, notificationID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000] = pendingIntent
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
        val constraintBulder=CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val calendar = Calendar.getInstance()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintBulder.build())
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
        datePicker.show(fragmentManager, "datePicker") } }