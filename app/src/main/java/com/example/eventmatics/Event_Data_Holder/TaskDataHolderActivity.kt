package com.example.eventmatics.Event_Data_Holder

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.TaskDataHolderData
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.data_class.TaskDataHolder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskDataHolderActivity : AppCompatActivity() {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderData
    private lateinit var paymentList: MutableList<TaskDataHolder>
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_data_holder)
        recyclerView = findViewById(R.id.TaskDatarec)
        taskAdd=findViewById(R.id.taskAdd)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomnav)
        bottomnav.background=null

        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recycler view
        paymentList = mutableListOf()
        adapter = TaskDataHolderData(paymentList)
        recyclerView?.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val name=intent.getStringExtra("name")
        val note=intent.getStringExtra("note")
        if (name !=null &&note != null) {
            paymentList.add(TaskDataHolder(name.toString(),note.toString(),"12","pp"))
            adapter.notifyDataSetChanged()
        }

        taskAdd.setOnClickListener {
            Intent(this,TaskDetails::class.java).also { startActivity(it) }
        }

    }

//    private fun AddTaskValue() {
//        val name=intent?.getStringExtra("name")
//        val note=intent?.getStringExtra("note")
//
//        val data=TaskDataHolder(name.toString(),note.toString(),"12/04/03","Rec")
//        paymentList.add(data)
//        adapter.notifyDataSetChanged()
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            R.id.Check->{
//                AddTaskValue()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}