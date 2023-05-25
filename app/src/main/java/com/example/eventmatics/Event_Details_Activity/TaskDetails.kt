package com.example.eventmatics.Event_Details_Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.TaskAdapter
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Subtask_info
import com.example.eventmatics.fragments.TaskFragment

class TaskDetails : AppCompatActivity(),TaskFragment.UserDataListener {
    val fragmentManager:FragmentManager=supportFragmentManager
    lateinit var TaskNameET:EditText
    lateinit var TaskNoteET:EditText
    lateinit var taskdate:TextView
    lateinit var category_button:AppCompatButton
    lateinit var TaskPendingbut:AppCompatButton
    lateinit var TaskCombut:AppCompatButton
    lateinit var TaskAdd:ImageView
    lateinit var subtaskrcv:RecyclerView
    lateinit var adapter:TaskAdapter
    lateinit var Tasklist:MutableList<Subtask_info>

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
        TaskAdd=findViewById(R.id.TaskAdd)

        TaskAdd.setOnClickListener {
            subtaskadd()
        }
        //Recyccler view code
        subtaskrcv=findViewById(R.id.subtaskrcv)
        Tasklist= mutableListOf()
        adapter= TaskAdapter(Tasklist)
        subtaskrcv.adapter=adapter
        subtaskrcv.layoutManager=LinearLayoutManager(this)

    }

    private fun subtaskadd() {
        val bottomsheet=TaskFragment()
        bottomsheet.setUserEnterDataListener(this)
        bottomsheet.show(fragmentManager,"bottomsheet")
    }
    override fun onUserDataEnter(userdata: Subtask_info) {
        Tasklist.add(userdata)
        adapter.notifyDataSetChanged()
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
         else->super.onOptionsItemSelected(item)
        }
    }


}