package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.TaskEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Task
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskDetails : AppCompatActivity(){
    val fragmentManager:FragmentManager=supportFragmentManager
    lateinit var TaskNameET:EditText
    lateinit var taskNote:EditText
    lateinit var taskdate:TextView
    lateinit var category_button:ImageView
    lateinit var categoryedit:TextView
    lateinit var taskPendingbut:AppCompatButton
    lateinit var TaskCombut:AppCompatButton
    var taskStatus:String=""
    var updatedtaskStatus:String=""
    var TaskStatus:Boolean=false
    var taskButtonClicked: Boolean = false
val spinnerItems = arrayOf(
    "Accessories", "Accommodation", "Attire & accessories", "Ceremony",
    "Flower & Decor", "Health & Beauty", "Jewelry", "Miscellaneous",
    "Music & Show", "Photo & Video", "Reception", "Transportation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        val toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitle("Task Details")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        TaskNameET=findViewById(R.id.TaskNameET)
        taskNote=findViewById(R.id.TaskNoteET)
        taskdate=findViewById(R.id.taskdate)
        categoryedit=findViewById(R.id.categoryedit)
        category_button=findViewById(R.id.Taskcategory_button)
        taskPendingbut=findViewById(R.id.TaskPendingbut)
        TaskCombut=findViewById(R.id.Taskcombut)

        val selectedTask: TaskEntity? = intent.getParcelableExtra("selected_task")
        if (selectedTask != null) {
            TaskNameET.setText(selectedTask.taskName)
            categoryedit.text = selectedTask.category
            taskNote.setText(selectedTask.taskNote)
            taskdate.setText(selectedTask.taskDate)
            if(selectedTask.taskStatus=="Pending"){
                setButtonBackground(taskPendingbut,true)
                setButtonBackground(TaskCombut,false)
            }
            if(selectedTask.taskStatus=="Completed"){
                setButtonBackground(taskPendingbut,false)
                setButtonBackground(TaskCombut,true)
            } }
        else{
            GlobalScope.launch(Dispatchers.IO){
            val dao = RoomDatabaseManager.getEventsDao(applicationContext)
            val event=dao.getEventData(1)
            val date=event!!.date
            taskdate.setText(date)
            }
        }

        taskPendingbut.setOnClickListener {
            TaskStatus=false
            taskButtonClicked=true
            setButtonBackground(taskPendingbut,true)
            setButtonBackground(TaskCombut,false)
        }
        TaskCombut.setOnClickListener {
            TaskStatus=true
            taskButtonClicked=true
            setButtonBackground(taskPendingbut,false)
            setButtonBackground(TaskCombut,true)
        }
        taskdate.setOnClickListener { showdatepicker() }
        category_button.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Select Category")
                .setSingleChoiceItems(spinnerItems, 0) { dialog, which ->
                    categoryedit.text=spinnerItems[which]
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }.show()
        } }

    fun setButtonBackground(button: Button, isSelected:Boolean){
        val BackgroundColor=if(isSelected) R.color.light_blue else R.color.light
        button.backgroundTintList=ContextCompat.getColorStateList(this,BackgroundColor)
    }

    private fun showdatepicker() {
        val constraint=CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datepicker=MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Task Date")
            .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
            .setCalendarConstraints(constraint.build())
            .build()

        datepicker.addOnPositiveButtonClickListener {selectedDate->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDate
            val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val selectedMonth = selectedCalendar.get(Calendar.MONTH)
            val selectedYear = selectedCalendar.get(Calendar.YEAR)
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            taskdate.setText(formattedDate)
        }
        datepicker.show(fragmentManager, "datePicker")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            R.id.Check->{
                val selectedTask: TaskEntity? = intent.getParcelableExtra("selected_task")
                if(selectedTask!=null){ UpdateData(selectedTask.id) }
                else{ addvaluetodatabase() }
                true
            }
         else->super.onOptionsItemSelected(item)
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun addvaluetodatabase(){
        val taskname=TaskNameET.text.toString()
        val category=categoryedit.text.toString()
        val TaskNoteET=taskNote.text.toString()
        val taskdate=taskdate.text.toString()
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO){
        if(TaskStatus){ taskStatus="Completed" }
        else{ taskStatus="Pending" }
        if(taskname.isEmpty()){ TaskNameET.error="Enter Task Name" }
        if(category.isEmpty()){ categoryedit.error="Enter Or Select a category" }
        if(taskname.isEmpty()&&category.isEmpty()){
            val rootView = findViewById<View>(android.R.id.content)
            val snackbar = Snackbar.make(rootView!!, "Enter Full Details", Snackbar.LENGTH_SHORT)
            val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snackbar.view.layoutParams = params
            snackbar.show()
        }
        else{
        val Task= TaskEntity(0,taskname,category,TaskNoteET,taskStatus,taskdate)
            dao.InsertTask(Task)
        finish()
        } } }
    private fun UpdateData(id: Long) {
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        val taskname = TaskNameET.text.toString()
        val category = categoryedit.text.toString()
        val TaskNoteET = taskNote.text.toString()
        val taskdate = taskdate.text.toString()
        GlobalScope.launch(Dispatchers.IO){

        if(!taskButtonClicked){
            val selectedTask: TaskEntity? = intent.getParcelableExtra("selected_task")
            val previousdata=selectedTask?.taskStatus
            Log.d("Button_Status","The Status Of  A Button is :$previousdata")
            updatedtaskStatus=previousdata.toString()
        }
        else if(TaskStatus){ updatedtaskStatus="Completed" }
        else{ updatedtaskStatus="Pending" }
        val task= TaskEntity(id,taskname,category,TaskNoteET,updatedtaskStatus,taskdate)
            dao.updateTask(task)
        finish()
    }
        }
}
