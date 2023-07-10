package com.example.eventmatics.Events_Data_Holder_Activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class TaskDataHolderActivity : AppCompatActivity() {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderData
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val tasklist: MutableList<Task> = mutableListOf()

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
//        swipeRefreshLayout.setOnRefreshListener {
//            val databsename=getSharedPreference(this,"databasename").toString()
//            val db=LocalDatabase(this,databsename)
//            val tasklist=db.getAllTasks()
//            if(tasklist!=null){
//                //Recycler view
//                adapter = TaskDataHolderData(tasklist)
//                recyclerView?.adapter = adapter
//                recyclerView.layoutManager = LinearLayoutManager(this)
//                swipeRefreshLayout.isRefreshing=false
//            }
//        }
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databsename=getSharedPreference(this,"databasename").toString()

                val db= Firebase.firestore
                val documentref=db.collection(databsename).document("Task")
                documentref.get()
                    .addOnSuccessListener { document->
                        if(document!=null && document.exists()){
                            val task=document.toObject(Task::class.java)
                            task?.let {
                                tasklist.clear()
                                tasklist.add(it)
                                adapter.notifyDataSetChanged()
                            }

                        }
                        else{
                            Log.d(TAG, "Document not found")

                        }
                    }
                    .addOnFailureListener { e ->
                        // Error fetching event
                        Log.e(TAG, "Error fetching event", e)
                    }
                swipeRefreshLayout.isRefreshing=false

            },3000)

        }
//        showTaskData()
        fetchdata()
    }

    private fun fetchdata() {
        val databsename = getSharedPreference(this, "databasename").toString()

        val db = Firebase.firestore
        val documentref = db.collection(databsename).document("Task")
        documentref.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val task = document.toObject(Task::class.java)
                    task?.let {
                        tasklist.clear()
                        tasklist.add(it)
                        // Initialize the adapter if it is not already initialized
                        if (!::adapter.isInitialized) {
                            adapter = TaskDataHolderData(tasklist)
                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(this)
                        } else {
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Log.d(TAG, "Document not found")
                }
            }
            .addOnFailureListener { e ->
                // Error fetching event
                Log.e(TAG, "Error fetching event", e)
            }
    }


//    private fun showTaskData() {
//        val databsename=getSharedPreference(this,"databasename").toString()
//        val db=LocalDatabase(this,databsename)
//        val tasklist=db.getAllTasks()
//        if(tasklist!=null){
//            //Recycler view
//            adapter = TaskDataHolderData(tasklist)
//            recyclerView?.adapter = adapter
//            recyclerView.layoutManager = LinearLayoutManager(this)
//            swipeRefreshLayout.isRefreshing=false
//        }
//    }

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