package com.example.eventmatics.Event_Details_Activity

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.data_class.SpinnerItem
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class TaskDetails : AppCompatActivity(){
    val fragmentManager:FragmentManager=supportFragmentManager
    lateinit var TaskNameET:EditText
    lateinit var taskNote:EditText
    lateinit var taskdate:TextView
    lateinit var category_button:TextView
    lateinit var taskPendingbut:AppCompatButton
    lateinit var TaskCombut:AppCompatButton
    var taskStatus:String=""
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
        taskNote=findViewById(R.id.TaskNoteET)
        taskdate=findViewById(R.id.taskdate)
        category_button=findViewById(R.id.Taskcategory_button)
        taskPendingbut=findViewById(R.id.TaskPendingbut)
        TaskCombut=findViewById(R.id.Taskcombut)

        val selectedTask: Task? = intent.getParcelableExtra("selected_task")
        if (selectedTask != null) {
            TaskNameET.setText(selectedTask.taskName)
            category_button.text = selectedTask.category
            taskNote.setText(selectedTask.taskNote)
            taskdate.setText(selectedTask.taskDate)

        }
        else{
            val today= Calendar.getInstance()
            val formateddate="${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.MONTH)+1}/${today.get(Calendar.YEAR)}"
            taskdate.setText(formateddate)
        }

        taskPendingbut.setOnClickListener {
            setButtonBackground(taskPendingbut,true)
            setButtonBackground(TaskCombut,false)
        }
        TaskCombut.setOnClickListener {
            setButtonBackground(taskPendingbut,false)
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
                val selectedTask: Task? = intent.getParcelableExtra("selected_task")
                if(selectedTask!=null){
                    UpdateData(selectedTask.id)
                }else{
                addvaluetodatabase()
                }
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
        val TaskNoteET=taskNote.text.toString()
        val taskdate=taskdate.text.toString()


        if (taskPendingbut.isClickable) {
            taskStatus = taskPendingbut.text.toString()
        }
        if(TaskCombut.isClickable) {
            taskStatus = TaskCombut.text.toString()
        }
        Log.d("TaskStaus:",taskStatus)
        val Task=Task(0,taskname,category,TaskNoteET,taskStatus,taskdate)
        Db.createTask(Task)
        Toast.makeText(this, "Task Added successfully", Toast.LENGTH_SHORT).show()

        finish()

}
    private fun UpdateData(id: Long) {
        val databasename=getSharedPreference(this,"databasename").toString()
        val Db=LocalDatabase(this,databasename)
        val taskname = TaskNameET.text.toString()
        val category = category_button.text.toString()
        val TaskNoteET = taskNote.text.toString()
        val taskdate = taskdate.text.toString()

        if (taskPendingbut.isSelected) {
            taskStatus = "Pending"
        } else if (TaskCombut.isSelected) {
            taskStatus = "Completed"
        }
        val task=Task(id,taskname,category,TaskNoteET,taskStatus,taskdate)
        Db.updateTask(task)
        Toast.makeText(this, "Task Updated successfully", Toast.LENGTH_SHORT).show()

        finish()
    }
}
