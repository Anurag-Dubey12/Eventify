package com.example.eventmatics.Events_Data_Holder_Activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.TaskDataHolderAdpater
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.SwipeGesture.SwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class TaskDataHolderActivity : AppCompatActivity(), TaskDataHolderAdpater.OnItemClickListener {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderAdpater
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var isRecyclerViewEmpty = true
    private  val NUMBER_OF_COLUMNS = 6
    private var tasklist: MutableList<Task> = mutableListOf()
    override fun onItemClick(task: Task) {
        // Open TaskDetails activity and pass the selected task's data
        Intent(this, TaskDetails::class.java).apply {
            putExtra("selected_task", task)
            startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_data_holder)
        recyclerView = findViewById(R.id.TaskDatarec)
        taskAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Check if the first visible item index is at the top
                val isAtTop = recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled =
                    !isAtTop
            // Enable/disable the SwipeRefreshLayout based on scroll position
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            val databsename=getSharedPreference(this,"databasename").toString()
            val db=LocalDatabase(this,databsename)
            tasklist=db.getAllTasks()
            isRecyclerViewEmpty = tasklist.isNullOrEmpty()
            if(tasklist!=null){
                //Recycler view
                adapter = TaskDataHolderAdpater(this,tasklist,this)
                recyclerView?.adapter = adapter

                recyclerView.layoutManager = LinearLayoutManager(this)
                swipeRefreshLayout.isRefreshing=false
            }
            invalidateOptionsMenu()
        }
        swipeRefreshLayout.setProgressViewEndTarget(true,150)
        showTaskData()
//        loadOriginalTaskList()

    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val pdfReportItem = menu.findItem(R.id.pdfreport)
        val check = menu.findItem(R.id.Check)
        pdfReportItem.isVisible = !isRecyclerViewEmpty
        return true
    }

    override fun onResume() {
        super.onResume()

        val databsename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databsename)
        tasklist=db.getAllTasks()
        isRecyclerViewEmpty=tasklist.isNullOrEmpty()
        if(tasklist!=null){
            //Recycler view
            adapter = TaskDataHolderAdpater(this,tasklist,this)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
//            swipeRefreshLayout.isRefreshing=false

        }

        invalidateOptionsMenu()
    }
    private fun showTaskData() {

        val databsename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databsename)
        tasklist=db.getAllTasks()
        isRecyclerViewEmpty=tasklist.isNullOrEmpty()
        if(tasklist!=null){
            //Recycler view
            adapter = TaskDataHolderAdpater(this,tasklist,this)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

//            swipeRefreshLayout.isRefreshing=false
        }
        val swipe=object :SwipeToDelete(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                var actionBtn=false
                when(direction){
                    ItemTouchHelper.LEFT->{
                        if(position!=RecyclerView.NO_POSITION){
                            val deleteitem=tasklist[position]
                            db.deleteTask(deleteitem)
                            adapter.notifyItemRemoved(position)


                            val snackbar=Snackbar.make(this@TaskDataHolderActivity.recyclerView,"Item Delete",Snackbar.LENGTH_SHORT)
                                .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        super.onDismissed(transientBottomBar, event)
                                        recreate()
                                    }
                                    override fun onShown(transientBottomBar: Snackbar?) {
                                        transientBottomBar?.setAction("UNDO"){
                                            tasklist.add(position,deleteitem)
                                            adapter.notifyItemInserted(position)
                                            actionBtn=true
                                        }
                                        super.onShown(transientBottomBar)
                                    }

                                }).apply {
                                    animationMode = Snackbar.ANIMATION_MODE_FADE
                                }
                            snackbar.setActionTextColor(
                                ContextCompat.getColor(
                                    this@TaskDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                                )
                            )
                            snackbar.show()

                        }
                    }

                    ItemTouchHelper.RIGHT->{
                        if(position!=RecyclerView.NO_POSITION){
                        val Task=tasklist[position]
                        val position=viewHolder.adapterPosition

                            val TaskStatus=Task.taskStatus

                            val NewStatus=if(TaskStatus=="Pending") "Completed" else "Pending"

                            val rowaffected=db.UpdateTaskStatus(Task.id,NewStatus)

                            if(rowaffected>0){
                        val snackbar=Snackbar.make(this@TaskDataHolderActivity.recyclerView
                            ,"Item Paid",Snackbar.LENGTH_LONG)
                            .addCallback(object :BaseTransientBottomBar.BaseCallback<Snackbar>(){
                                override fun onDismissed(
                                    transientBottomBar: Snackbar?,
                                    event: Int
                                ) {
                                    super.onDismissed(transientBottomBar, event)
                                }

                                override fun onShown(transientBottomBar: Snackbar?) {
                                    transientBottomBar?.setAction("UNDO"){
                                        val previousPaidStatus = if (NewStatus == "Pending") "Completed" else "Pending"
                                        db.UpdateTaskStatus(Task.id, previousPaidStatus)
                                    }
                                    recreate()
                                    super.onShown(transientBottomBar)
                                }
                            })
                            .apply {
                                animationMode=Snackbar.ANIMATION_MODE_SLIDE
                            }
                        snackbar.setActionTextColor(
                            ContextCompat.getColor(
                                this@TaskDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                            )
                        )
                        snackbar.show()
                    }
                }
            }
        }}}
        val touchHelper=ItemTouchHelper(swipe)
        touchHelper.attachToRecyclerView(recyclerView)
        invalidateOptionsMenu()
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.holder,menu)
        val searchItem = menu?.findItem(R.id.action_search)
        // Set up the SearchView
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint = "Search"

        // Handle query submission and text changes
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchTask(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchTask(newText)
                return true
            }
        })
        return true
    }
    private fun loadOriginalTaskList() {
        val databsename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databsename)
        tasklist = db.getAllTasks().toMutableList()

        // Update the RecyclerView adapter with the original task list
        adapter.setData(tasklist)
    }
    private fun searchTask(query: String) {
        val databsename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databsename)
        val filteredList = db.searchTask(query)

        // Update the RecyclerView adapter with the filtered task list
        adapter.setData(filteredList as MutableList<Task>)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            R.id.pdfreport->{

                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}