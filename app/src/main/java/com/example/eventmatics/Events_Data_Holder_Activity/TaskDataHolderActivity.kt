package com.example.eventmatics.Events_Data_Holder_Activity


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.TaskDataHolderAdpater
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.TaskEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Task
import com.example.eventmatics.SwipeGesture.SwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskDataHolderActivity : AppCompatActivity(), TaskDataHolderAdpater.OnItemClickListener {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderAdpater
    private lateinit var recyclerView: RecyclerView
    private lateinit var Data_Not_found: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var isRecyclerViewEmpty = true
    lateinit var bmp:Bitmap
    lateinit var scalebmp:Bitmap
    private var tasklist: MutableList<TaskEntity> = mutableListOf()
    val PERMISSION_CODE=101
    override fun onItemClick(task: TaskEntity) {
        Intent(this, TaskDetails::class.java).apply {
            putExtra("selected_task", task)
            startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_data_holder)
        recyclerView = findViewById(R.id.TaskDatarec)
        Data_Not_found = findViewById(R.id.data_not_found)
        taskAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        GlobalScope.launch(Dispatchers.Main){
            RoomDatabaseManager.initialize(applicationContext)
        }
        showTaskData()
        taskAdd.setOnClickListener { Intent(this,TaskDetails::class.java).also { startActivity(it) } }
        Data_Not_found.visibility=if(recyclerView.adapter?.itemCount==0) View.VISIBLE else View.GONE
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    showSortOptions()
                    true
                }
                R.id.Filter ->{
                    showFilterOption()
                    true
                }
                else -> false
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Check if the first visible item index is at the top
                val isAtTop = recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled =
                    !isAtTop
            } })
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                showTaskData()
                swipeRefreshLayout.isRefreshing=false
            },1000)
            showTaskData()
            invalidateOptionsMenu()
        }
        swipeRefreshLayout.setProgressViewEndTarget(true,150)
        showTaskData()
    }
    override fun onResume() {
        super.onResume()
       showTaskData()
        invalidateOptionsMenu()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun showTaskData() {
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO){
        tasklist=dao.getAllTasks()
        isRecyclerViewEmpty=tasklist.isNullOrEmpty()
            runOnUiThread{
            if(tasklist!=null){
            adapter = TaskDataHolderAdpater(applicationContext,tasklist,this@TaskDataHolderActivity)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@TaskDataHolderActivity)
        }

        val swipe=object :SwipeToDelete(this@TaskDataHolderActivity){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                var actionBtn=false
                when(direction){
                    ItemTouchHelper.LEFT->{
                        if(position!=RecyclerView.NO_POSITION){
                            val deleteitem=tasklist[position]

                            adapter.notifyItemRemoved(position)
                            MaterialAlertDialogBuilder(this@TaskDataHolderActivity)
                                .setTitle("Delete Item")
                                .setMessage("Do you want to delete this item?")
                                .setPositiveButton("Delete") { dialog, which ->
                                    adapter.removeItem(position)
                                    GlobalScope.launch(Dispatchers.IO){
                                    dao.deleteTask(deleteitem)
                                    }
                                    actionBtn = true
                                }
                                .setNegativeButton("Cancel") { dialog, which ->
                                    tasklist.add(position, deleteitem)
                                    adapter.notifyItemInserted(position) }.show()
                        } }
                    ItemTouchHelper.RIGHT->{
                        if(position!=RecyclerView.NO_POSITION){
                            val Task=tasklist[position]
                            val position=viewHolder.adapterPosition

                            val TaskStatus=Task.taskStatus

                            val NewStatus=if(TaskStatus=="Pending") "Completed" else "Pending"
                            when(TaskStatus){
                                "Pending"-> MaterialAlertDialogBuilder(this@TaskDataHolderActivity)
                                    .setTitle("Task Status Update")
                                    .setMessage("Is Your Task has Completed?")
                                    .setPositiveButton("Yes"){dialog,_-> dao.updateTaskStatus(Task.id,NewStatus)
                                        recreate()
                                    }
                                    .setNeutralButton("No"){dialog,_-> dialog.cancel()
                                        recreate()
                                    }.show()
                                "Completed"->MaterialAlertDialogBuilder(this@TaskDataHolderActivity)
                                    .setTitle("Task Status Update")
                                    .setMessage("Is Your Task has Pending?")
                                    .setPositiveButton("Yes"){dialog,_-> dao.updateTaskStatus(Task.id,NewStatus)
                                        recreate()
                                    }
                                    .setNeutralButton("No"){dialog,_-> dialog.cancel()
                                        recreate() }.show() }
                        } } }}}
        val touchHelper= ItemTouchHelper(swipe)
        touchHelper.attachToRecyclerView(recyclerView)
        invalidateOptionsMenu()
    }
        }}
    private fun showSortOptions() {
        val sortvalue = arrayOf("Name", "Date")
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Sort Data")
            .setNeutralButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .setSingleChoiceItems(sortvalue, -1) { dialog, which ->
            when (sortvalue[which]) {
                "Name" -> {
                    tasklist.sortBy { it.taskName }
                    adapter.notifyDataSetChanged()
                }
                "Date" -> {
                    tasklist.sortBy { it.taskDate }
                    adapter.notifyDataSetChanged()
                }
            }
            dialog.dismiss()
            adapter.notifyDataSetChanged()
        }
        dialog.show() }

    private fun showFilterOption() {
        val FilterOption= arrayOf("Pending","Completed")
        MaterialAlertDialogBuilder(this)
            .setTitle("Filter Data")
            .setSingleChoiceItems(FilterOption,-1){dialog,which->
                val selectedFilter = FilterOption[which]
                val filteredList = tasklist.filter { task -> task.taskStatus == selectedFilter }
                adapter.setData(filteredList.toMutableList())
                dialog.dismiss()
            }
            .setNeutralButton("Cancel"){dialog,_-> dialog.dismiss() }
            .show()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.holder,menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { searchTask(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean { searchTask(newText)
                return true
            } })
        return true
    }
    private fun searchTask(query: String) {
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO){
        val filteredList = dao.searchTasks(query)
        adapter.setData(filteredList as MutableList<TaskEntity>)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }


}