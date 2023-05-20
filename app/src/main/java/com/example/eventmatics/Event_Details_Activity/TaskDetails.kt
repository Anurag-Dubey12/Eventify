package com.example.eventmatics.Event_Details_Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.fragments.TaskFragment

class TaskDetails : AppCompatActivity() {
    val fragmentManager:FragmentManager=supportFragmentManager
    lateinit var TaskNameET:EditText
    lateinit var TaskNoteET:EditText
    lateinit var taskdate:TextView
    lateinit var category_button:AppCompatButton
    lateinit var TaskPendingbut:AppCompatButton
    lateinit var TaskCombut:AppCompatButton
    lateinit var TaskAdd:ImageView
    lateinit var subtaskrcv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        TaskNameET=findViewById(R.id.TaskNameET)
        TaskNoteET=findViewById(R.id.TaskNoteET)
        taskdate=findViewById(R.id.taskdate)
        category_button=findViewById(R.id.Taskcategory_button)
        TaskPendingbut=findViewById(R.id.TaskPendingbut)
        TaskCombut=findViewById(R.id.Taskcombut)
        TaskAdd=findViewById(R.id.TaskAdd)
        subtaskrcv=findViewById(R.id.subtaskrcv)


        TaskAdd.setOnClickListener {
            subtaskadd()
        }

    }

    private fun subtaskadd() {
        val bottomsheet=TaskFragment()
        bottomsheet.show(fragmentManager,"bottomsheet")
    }
}