package com.example.eventmatics.Events_Data_Holder_Activity

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.TaskDataHolderData
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class TaskDataHolderActivity : AppCompatActivity(), TaskDataHolderData.OnItemClickListener {
    lateinit var taskAdd:FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    private lateinit var adapter: TaskDataHolderData
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var isRecyclerViewEmpty = true
    private  val NUMBER_OF_COLUMNS = 6
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
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Check if the first visible item index is at the top
                val isAtTop = recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled =
                    !isAtTop // Enable/disable the SwipeRefreshLayout based on scroll position
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
                adapter = TaskDataHolderData(this,tasklist,this)
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
            adapter = TaskDataHolderData(this,tasklist,this)
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
            adapter = TaskDataHolderData(this,tasklist,this)
            recyclerView?.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            swipeRefreshLayout.isRefreshing=false
        }
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
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            R.id.pdfreport->{
                val databsename = getSharedPreference(this, "databasename").toString()
                val db = LocalDatabase(this, databsename)
                val tasklist = db.getAllTasks()
                if (tasklist != null) {
                    val pdfFile = generatePdfReport(this, tasklist)
                    val pdfUri = insertPdfIntoMediaStore(this, pdfFile)
                    if (pdfUri != null) {
                        val pdfIntent = Intent(Intent.ACTION_VIEW)
                        pdfIntent.setDataAndType(pdfUri, "application/pdf")
                        pdfIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        startActivity(Intent.createChooser(pdfIntent, "Open PDF with"))
                    } else {
                        Toast.makeText(this, "Failed to open PDF", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No data available to generate the report", Toast.LENGTH_SHORT).show()
                }
                true

            }
            else->super.onOptionsItemSelected(item)
        }
    }
    private fun insertPdfIntoMediaStore(context: Context, file: File): Uri? {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        context.grantUriPermission(
            "com.android.systemui", // Package name of the app you want to give access to
            uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
        return uri
    }
    fun generatePdfReport(context: Context, dataList: List<Task>): File {


        val fileName = "report.pdf"
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(filePath, fileName)

        val document = Document()
        val outputStream = FileOutputStream(file)
        val writer = PdfWriter.getInstance(document, outputStream)

        document.open()

        // Create the PDF table with 6 columns
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
            table.addCell(PdfPCell(Phrase("Data 6", normalFont))) // You can replace "Data 6" with the content of your sixth column
        }

        // Add the table to the document
        document.add(table)

        document.close()
        writer.close()

        return file
    }
}