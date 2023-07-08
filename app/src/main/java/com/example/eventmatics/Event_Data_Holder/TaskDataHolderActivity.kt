package com.example.eventmatics.Event_Data_Holder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.TaskDataHolderData
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskDataHolderActivity : AppCompatActivity() {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderData
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_data_holder)
        recyclerView = findViewById(R.id.TaskDatarec)
        taskAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskAdd.setOnClickListener {
            Intent(this,TaskDetails::class.java).also { startActivity(it) }
        }
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    showSortOptions()
                    true
                }
                else -> false
            }
        }
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            val databsename=getSharedPreference(this,"databasename").toString()
            val db=LocalDatabase(this,databsename)
            val tasklist=db.getAllTasks()
            if(tasklist!=null){
                //Recycler view
                adapter = TaskDataHolderData(tasklist)
                recyclerView?.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
                swipeRefreshLayout.isRefreshing=false
            }
        }
        showTaskData()
    }
    private fun showTaskData() {
        val databsename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databsename)
        val tasklist=db.getAllTasks()
        if(tasklist!=null){
            //Recycler view
            adapter = TaskDataHolderData(tasklist)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            swipeRefreshLayout.isRefreshing=false
        }
    }

    fun getSharedPreference(context:Context,key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }
    private fun showSortOptions() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.sortpopup, null)

        dialogBuilder.setView(view)

        val nameAscending = view.findViewById<TextView>(R.id.name_ascen)
        val nameDescending = view.findViewById<TextView>(R.id.name_decen)
        val amountAscending = view.findViewById<TextView>(R.id.Amount_ascen)
        val amountDescending = view.findViewById<TextView>(R.id.Amount_decen)

        dialogBuilder.setTitle("Select list order type")
        val dialog = dialogBuilder.create()
        dialog.show()

        nameAscending.setOnClickListener {
            dialog.dismiss()
        }

        nameDescending.setOnClickListener {
            dialog.dismiss()
        }

        amountAscending.setOnClickListener {
            dialog.dismiss()
        }

        amountDescending.setOnClickListener {
            dialog.dismiss()
        }
    }

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