package com.example.eventmatics.Events_Data_Holder_Activity


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
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
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.SwipeGesture.AbstractClass.SwipeControllerActions
import com.example.eventmatics.SwipeGesture.SwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var Data_Item_Selected: CheckBox
    private var isRecyclerViewEmpty = true
    private  val NUMBER_OF_COLUMNS = 6
    lateinit var bmp:Bitmap
    private lateinit var swipeController: SwipeToDelete
    lateinit var scalebmp:Bitmap
    private var tasklist: MutableList<Task> = mutableListOf()
    val PERMISSION_CODE=101
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
        showTaskData()
        taskAdd.setOnClickListener {
            Intent(this,TaskDetails::class.java).also { startActivity(it) }
        }
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
            // Enable/disable the SwipeRefreshLayout based on scroll position
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                showTaskData()
                swipeRefreshLayout.isRefreshing=false
            },1000
            )
            showTaskData()
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
       showTaskData()
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

                            adapter.notifyItemRemoved(position)
                            MaterialAlertDialogBuilder(this@TaskDataHolderActivity)
                                .setTitle("Delete Item")
                                .setMessage("Do you want to delete this item?")
                                .setPositiveButton("Delete") { dialog, which ->
                                    adapter.removeItem(position)
                                    db.deleteTask(deleteitem)
                                    actionBtn = true
//                                    recreate()
                                }
                                .setNegativeButton("Cancel") { dialog, which ->
                                    tasklist.add(position, deleteitem)
                                    adapter.notifyItemInserted(position)
                                }

                                .show()
                        }
                    }
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
                                    .setPositiveButton("Yes"){dialog,_->
                                        db.UpdateTaskStatus(Task.id,NewStatus)
                                        recreate()
                                    }
                                    .setNeutralButton("No"){dialog,_->
                                        dialog.cancel()
                                        recreate()
                                    }
//                                    .setNegativeButton("Pending"){dialog,_->
//                                        db.UpdateTaskStatus(Task.id,NewStatus)
//                                        recreate()
//                                    }
                                    .show()
                                "Completed"->MaterialAlertDialogBuilder(this@TaskDataHolderActivity)
                                    .setTitle("Task Status Update")
                                    .setMessage("Is Your Task has Pending?")
                                    .setPositiveButton("Yes"){dialog,_->
                                        db.UpdateTaskStatus(Task.id,NewStatus)
                                        recreate()
                                    }
                                    .setNeutralButton("No"){dialog,_->
                                        dialog.cancel()
                                        recreate()
                                    }
//                                    .setNegativeButton("Pending"){dialog,_->
//                                        db.UpdateTaskStatus(Task.id,NewStatus)
//                                        recreate()
//                                    }
                                    .show()
                            }


                        }
                    }
                }}}
        val touchHelper= ItemTouchHelper(swipe)
        touchHelper.attachToRecyclerView(recyclerView)
        invalidateOptionsMenu()
    }

    fun getSharedPreference(context:Context,key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }
    private fun showSortOptions() {

        val sortvalue= arrayOf("Name","Date")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort Data")
            .setMessage("Select In Which Format you want to sort the data")

            .setSingleChoiceItems(sortvalue,1){dialog,which->
                when(sortvalue[which]){
                    "Name"->{
                        tasklist.sortBy { it.taskName }
                        adapter.notifyDataSetChanged()
                        }
                    "Date"->{tasklist.sortBy { it.taskDate }

                        adapter.notifyDataSetChanged()}
                }
                dialog.dismiss()
                adapter.notifyDataSetChanged()
            }
            .setNeutralButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .show()
    }
    private fun showFilterOption() {
        val FilterOption= arrayOf("Pending","Completed")
        MaterialAlertDialogBuilder(this)
            .setTitle("Filter Data")
            .setSingleChoiceItems(FilterOption,-1){dialog,which->
                val selectedFilter = FilterOption[which]

                val filteredList = tasklist.filter { task ->
                    task.taskStatus == selectedFilter
                }
                adapter.setData(filteredList.toMutableList())
                dialog.dismiss()
            }
            .setNeutralButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .show()
    }
    fun gettaskstatus(status:String):Int{
        return when(status){
            "Pending"->0
            "Completed"->1
            else->2
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
                if(checkPermission()){
                    GeneratedPDF()
                }
                else{
                    requestPermission()
                }
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun GeneratedPDF(){
        val database=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,database)
        val Task=db.getAllTasks()
        val Event=db.getEventData(1)
        val name=Event?.name
        val pagewidth=792
        val pageheight=1120

        bmp=BitmapFactory.decodeResource(resources,R.drawable.logo)
        scalebmp=Bitmap.createScaledBitmap(bmp,190,190,false)

        val pdfDocument=PdfDocument()
        val paint=Paint()
        val title=Paint()

        val pageInfo:PdfDocument.PageInfo=PdfDocument.PageInfo.Builder(pagewidth,pageheight,1).create()
        val page:PdfDocument.Page=pdfDocument.startPage(pageInfo)

        val canvas:Canvas=page.canvas
        canvas.drawBitmap(scalebmp,20F,0F,paint)
        title.textSize=20F
        title.color=ContextCompat.getColor(this,R.color.black)
        canvas.drawText("Task Report",209F,50F,title)

        val starty=200F
        val lineHeight=50F

//        val column= listOf("Family Name","No.of Family Member","NOte","Invitation Status"
//        ,"Acceptance Status","Phone Number(Head of The Family)","Address")
        val column= listOf("No","Name","Task Category","Note"
            ,"Task Status","Task Date")

        val borderPaint=Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.color=Color.BLACK
        borderPaint.strokeWidth=2f

        val headerPaint = Paint()
        headerPaint.textSize = 20F
        headerPaint.color = Color.BLACK
        headerPaint.textAlign = Paint.Align.CENTER
        val HeaderX=pagewidth.toFloat()/2
        val headerY=starty

        for((columnIndex,columns) in column.withIndex()){
            val cellwidth=pagewidth/column.size
            val cellheight=lineHeight

            val cellX=columnIndex*cellwidth
            val cellY=headerY-cellheight/2

            canvas.drawRect(
                cellX.toFloat(),cellY,(cellX+cellwidth).toFloat(),(cellY+cellheight),borderPaint
            )
            canvas.drawText(columns,(cellX+cellwidth/2).toFloat(),cellY+cellheight/2,headerPaint)
        }

        val dataPaint=Paint()
        dataPaint.textSize=12F
        dataPaint.color=Color.BLACK
        dataPaint.textAlign=Paint.Align.CENTER

        val cellwidth=pagewidth/column.size
        val cellheight=lineHeight

        for((index,Task) in Task.withIndex()){
            val xpos=0
            val ypos=starty+(index+1)*lineHeight

            val dataitems= listOf(
                Task.id.toString(),
                Task.taskName,
                Task.category,
                Task.taskNote,
                Task.taskStatus,
                Task.taskDate
            )
            for((index,data) in dataitems.withIndex()){
                val cellX=index*cellwidth
                val cellY=ypos-cellheight/2

                canvas.drawRect(
                    cellX.toFloat(),cellY,(cellX+cellwidth).toFloat(),(cellY+cellheight),borderPaint
                )
                canvas.drawText(data,(cellX+cellwidth/2).toFloat(),cellY+cellheight/2,dataPaint)
            }
        }
        pdfDocument.finishPage(page)

        val ParentDirectory= File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!ParentDirectory.exists()){
            ParentDirectory.mkdirs()
        }
        val EventDirectory=File(ParentDirectory,name)
        if(!EventDirectory.exists()){
            EventDirectory.mkdirs()
        }
        val TaskDirectory=File(EventDirectory,"Task")
        if(!TaskDirectory.exists()){
            TaskDirectory.mkdirs()
        }
        val timestamp=SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename="Task_$timestamp.pdf"
        val pdffilepath=File(TaskDirectory,filename)
        try{
            pdffilepath.createNewFile()
            pdfDocument.writeTo(FileOutputStream(pdffilepath))
            Toast.makeText(this, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "PDF file generated successfully.")
        }
        catch (e:Exception){
            e.printStackTrace()
            Log.d("PDF","Failed to generate PDF File:${e.message}")
        }
        pdfDocument.close()
    }
    fun checkPermission():Boolean{
        var WriteStrogePermission=ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )
        var readExternalStorage=ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )
        return WriteStrogePermission==PackageManager.PERMISSION_GRANTED && readExternalStorage==PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==PERMISSION_CODE){
            if(grantResults.size>0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    GeneratedPDF()
                }
                else{
                    Log.d("PDF", "Permission Denied.")

                }
            }
        }
    }

}