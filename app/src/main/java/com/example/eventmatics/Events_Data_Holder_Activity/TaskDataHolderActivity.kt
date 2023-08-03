package com.example.eventmatics.Events_Data_Holder_Activity

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
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
import com.example.eventmatics.PDF.TaskPDF
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.SwipeGesture.SwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.FontFactory
import com.itextpdf.text.PageSize
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class TaskDataHolderActivity : AppCompatActivity(), TaskDataHolderAdpater.OnItemClickListener {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderAdpater
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var isRecyclerViewEmpty = true
    private  val NUMBER_OF_COLUMNS = 6
    private var filteredList: MutableList<Task> = mutableListOf()
    override fun onItemClick(task: Task) {
        // Open TaskDetails activity and pass the selected task's data
        Intent(this, TaskDetails::class.java).apply {
            putExtra("selected_task", task)
            startActivity(this)
        }
    }
    companion object {
        private const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_data_holder)
        recyclerView = findViewById(R.id.TaskDatarec)
        taskAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
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
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            val databsename=getSharedPreference(this,"databasename").toString()
            val db=LocalDatabase(this,databsename)
            val tasklist=db.getAllTasks()
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

        showTaskData()
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
        val tasklist=db.getAllTasks()
        isRecyclerViewEmpty=tasklist.isNullOrEmpty()
        if(tasklist!=null){
            //Recycler view
            adapter = TaskDataHolderAdpater(this,tasklist,this)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            swipeRefreshLayout.isRefreshing=false

        }

        invalidateOptionsMenu()
    }
    private fun showTaskData() {

        val databsename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databsename)
        val tasklist=db.getAllTasks()
        isRecyclerViewEmpty=tasklist.isNullOrEmpty()
        if(tasklist!=null){
            //Recycler view
            adapter = TaskDataHolderAdpater(this,tasklist,this)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            swipeRefreshLayout.isRefreshing=false
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
                        val paiditem=tasklist[position]
                        val position=viewHolder.adapterPosition
                        tasklist.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        tasklist.add(paiditem)
                        adapter.notifyItemRemoved(position)

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
                                        tasklist.add(position,paiditem)
                                        adapter.notifyItemInserted(position)
                                        actionBtn=true
                                    }
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
        }
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
                // Perform search or any other actions
//                Toast.makeText(applicationContext, "Search query: $query", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return true
            }
        })
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            R.id.pdfreport->{
               if(ContextCompat.checkSelfPermission(this,
                   android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                       PackageManager.PERMISSION_GRANTED){


                val databsename = getSharedPreference(this, "databasename").toString()
                val db = LocalDatabase(this, databsename)
                val tasklist = db.getAllTasks()
                if (tasklist != null) {
                    val pdfFile = generatePdfReport(this, tasklist)
                    val pdfUri = savePdfToExternalStorage(this, pdfFile)
                    if (pdfUri != null) {
                        openPdfFile(pdfUri)
                    } else {
                        Toast.makeText(this, "Failed to save or open PDF", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No data available to generate the report", Toast.LENGTH_SHORT).show()
                }
               }else{
                   ActivityCompat.requestPermissions(this,
                   arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                       REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                   )
               }
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The permission is granted, proceed with PDF generation and saving
                // (same code as above)
            } else {
                // The permission is denied, show a message or perform alternative actions if needed
                Toast.makeText(this, "Write storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePdfToExternalStorage(context: Context, file: File): Uri? {
        val rootDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val reportFile = File(rootDir, "report.pdf")

        try {

            file.copyTo(reportFile, overwrite = true)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return insertPdfIntoMediaStore(context, reportFile)
    }
    private fun insertPdfIntoMediaStore(context: Context, file: File): Uri? {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        return try {
            val uri = contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            uri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    file.inputStream().copyTo(outputStream)
                }
            }
            uri
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    private fun openPdfFile(pdfUri: Uri) {
        val pdfIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(pdfUri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        if (pdfIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(pdfIntent, "Open PDF with"))
        } else {
            Toast.makeText(this, "No PDF viewer app installed", Toast.LENGTH_SHORT).show()
        }
    }
    fun generatePdfReport(context: Context, dataList: List<Task>): File {
        val fileName = "report.pdf"
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(filePath, fileName)

        val document = Document(PageSize.A4)
        val outputStream = FileOutputStream(file)
        val writer = PdfWriter.getInstance(document, outputStream)
        val eventHelper = TaskPDF()

        writer.pageEvent = eventHelper // Set the TaskPDF instance as the event listener

        document.open()

        // Create the PDF table
        val table = PdfPTable(NUMBER_OF_COLUMNS)
        table.widthPercentage = 100f
        val columnWidths = floatArrayOf(3f, 2f, 2f, 2f, 2f, 2f)
        table.setWidths(columnWidths)

        // Add table headers
        val headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        val headerTitles = arrayOf("Task Name", "Category", "Task Note", "Task Status", "Task Date", "Column 6")
        for (title in headerTitles) {
            val cell = PdfPCell(Phrase(title, headerFont))
            cell.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(cell)
        }

        // Add data rows to the table
        val normalFont = FontFactory.getFont(FontFactory.HELVETICA)

        for (task in dataList) {
            table.addCell(PdfPCell(Phrase(task.taskName, normalFont)))
            table.addCell(PdfPCell(Phrase(task.category, normalFont)))
            table.addCell(PdfPCell(Phrase(task.taskNote, normalFont)))
            table.addCell(PdfPCell(Phrase(task.taskStatus, normalFont)))
            table.addCell(PdfPCell(Phrase(task.taskDate, normalFont)))
        }

        // Add the table to the document
        document.add(table)

        document.close()
        writer.close()

        return file
    }
}