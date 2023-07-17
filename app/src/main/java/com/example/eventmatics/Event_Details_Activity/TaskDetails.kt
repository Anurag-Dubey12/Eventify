package com.example.eventmatics.Event_Details_Activity

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.data_class.SpinnerItem
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class TaskDetails : AppCompatActivity(){
    val fragmentManager:FragmentManager=supportFragmentManager
    lateinit var TaskNameET:EditText
    lateinit var TaskNoteET:EditText
    lateinit var taskdate:TextView
    lateinit var category_button:AppCompatButton
    lateinit var TaskPendingbut:AppCompatButton
    lateinit var TaskCombut:AppCompatButton
    val spinnerItems = listOf(
        SpinnerItem("Accessories"),
        SpinnerItem( "Accommodation"),
        SpinnerItem( "Attire & accessories"),
        SpinnerItem( "Ceremony"),
        SpinnerItem( "Flower & Decor"),
        SpinnerItem( "Health & Beauty"),
        SpinnerItem( "Jewelry"),
        SpinnerItem( "Miscellaneous"),
        SpinnerItem( "Music & Show"),
        SpinnerItem( "Photo & Video"),
        SpinnerItem( "Reception"),
        SpinnerItem( "Transportation")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        val toolbar:Toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        TaskNameET=findViewById(R.id.TaskNameET)
        TaskNoteET=findViewById(R.id.TaskNoteET)
        taskdate=findViewById(R.id.taskdate)
        category_button=findViewById(R.id.Taskcategory_button)
        TaskPendingbut=findViewById(R.id.TaskPendingbut)
        TaskCombut=findViewById(R.id.Taskcombut)

        val taskId = intent.getIntExtra("taskId", 1)
        if (taskId != -1) {

            val databasename = getSharedPreference(this, "databasename").toString()
            val db = LocalDatabase(this, databasename)
            val task = db.getTaskData(taskId)

            if (task != null) {
                // Update the fields in TaskDetails activity with the fetched data
                TaskNameET.setText(task.taskName)
                TaskNoteET.setText(task.taskNote)
                taskdate.setText(task.taskDate)
                // ...
                // Update other fields accordingly
            }
        }
        TaskPendingbut.setOnClickListener {
            setButtonBackground(TaskPendingbut,true)
            setButtonBackground(TaskCombut,false)
        }
        TaskCombut.setOnClickListener {
            setButtonBackground(TaskPendingbut,false)
            setButtonBackground(TaskCombut,true)
        }
        taskdate.setOnClickListener {
            showdatepicker()
        }
        category_button.setOnClickListener {
            ShowTaskCategory()
        }
    }

    private fun ShowTaskCategory() {
        val spinnerAdapter = CategoryAdapter(this, spinnerItems)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Select Category")
            .setAdapter(spinnerAdapter) { _, position ->
                val selectedItem = spinnerItems[position]
                category_button.text=selectedItem.text
            }
            .setNegativeButton("Cancel", null)
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun setButtonBackground(button: Button, isSelected:Boolean){
        val BackgroundColor=if(isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList=ContextCompat.getColorStateList(this,BackgroundColor)
    }

    private fun showdatepicker() {

        val datepicker=MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Task Date")
            .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
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
                addvaluetodatabase()
                true
            }
         else->super.onOptionsItemSelected(item)
        }
    }


    fun getSharedPreference(context: Context, key: String): String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }
    private fun addvaluetodatabase(){
        val databasename=getSharedPreference(this,"databasename").toString()
        val Db=LocalDatabase(this,databasename)
        val taskname=TaskNameET.text.toString()
        val category=category_button.text.toString()
        val TaskNoteET=TaskNoteET.text.toString()
        val taskdate=taskdate.text.toString()
        var Task_Status=""
        if(TaskPendingbut.isSelected){
            Task_Status="Pending"
        }
        if(TaskCombut.isSelected){
             Task_Status="Completed"
        }

        val Task=Task(0,taskname,category,TaskNoteET,Task_Status,taskdate)
        Db.createTask(Task)
        Toast.makeText(this, "Task Added successfully", Toast.LENGTH_SHORT).show()

        finish()

}

    override fun onDestroy() {
        super.onDestroy()

    }
}
