package com.example.eventmatics

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventLayoutAdapter
import com.example.eventmatics.Events_Data_Holder_Activity.BudgetDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.GuestDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.TaskDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.VendorDataHolderActivity
import com.example.eventmatics.Login_Activity.signin_account
import com.example.eventmatics.NavigationDrawer.PDF_Report
import com.example.eventmatics.NavigationDrawer.SettingActivity
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.example.eventmatics.fragments.DatabaseNameHolder
import com.example.eventmatics.fragments.EventAdding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfWriter
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toogle:ActionBarDrawerToggle
    private var countDownTimer:CountDownTimer?=null
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var taskImageButton: ImageButton
    private lateinit var budgetImageButton: ImageButton
    private lateinit var guestImageButton: ImageButton
    private lateinit var vendorImageButton: ImageButton
    private lateinit var budgetInfoCardView: CardView
    private lateinit var budgetShowTextView: TextView
    private lateinit var Eventshow: TextView
    private lateinit var EventTimerDisplay: TextView
    private lateinit var crossimg: ImageView
    private lateinit var eventshowhide: LinearLayout
    private lateinit var pendingAmountShowTextView: TextView
    private lateinit var paidAmountShowTextView: TextView
    private lateinit var eventaddbut: AppCompatButton
    private lateinit var adapter: EventLayoutAdapter
    private lateinit var bmp:Bitmap
    private lateinit var scalebmp:Bitmap
//    private lateinit var widgetButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dataAddedReceiver: BroadcastReceiver
    private lateinit var piechart:PieChart
    private val PERMISSION_CODE=101
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawerlayout)
        navView= findViewById(R.id.navView)
        eventRecyclerView = findViewById(R.id.Eventrec)
        taskImageButton = findViewById(R.id.task)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
        EventTimerDisplay = findViewById(R.id.EventTimerDisplay)
        crossimg = findViewById(R.id.crossimg)
        eventaddbut = findViewById(R.id.eventaddbut)
        budgetImageButton = findViewById(R.id.budget)
        eventshowhide = findViewById(R.id.eventshowhide)
        guestImageButton = findViewById(R.id.Guest)
        vendorImageButton = findViewById(R.id.Vendor)
        Eventshow = findViewById(R.id.eventnameshow)
        piechart = findViewById(R.id.piechart)
//        taskRecyclerView = findViewById(R.id.TaskRec)
        budgetInfoCardView = findViewById(R.id.budget_info)
        budgetShowTextView = findViewById(R.id.Budgetshow)
        pendingAmountShowTextView = findViewById(R.id.PendingAmountshow)
        paidAmountShowTextView = findViewById(R.id.PaidAmountshow)
//        widgetButton = findViewById(R.id.widgetbutton)

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        if (eventRecyclerView.adapter?.itemCount == 0) {
            val eventAdding = EventAdding(this, supportFragmentManager, null)
            eventAdding.show()
        } else {
            taskImageButton.setOnClickListener {
                if (eventRecyclerView.adapter?.itemCount==0){
                    val eventAdding = EventAdding(this, supportFragmentManager, null)
                    eventAdding.show()
                    Toast.makeText(this,"Create A Event Before Moving Futher",Toast.LENGTH_SHORT).show()
                }else{
                Intent(this, TaskDataHolderActivity::class.java).also { startActivity(it) }
                }
            }
            budgetImageButton.setOnClickListener {
                if (eventRecyclerView.adapter?.itemCount==0){
                    val eventAdding = EventAdding(this, supportFragmentManager, null)
                    eventAdding.show()
                    Toast.makeText(this,"Create A Event Before Moving Futher",Toast.LENGTH_SHORT).show()
                }else{
                Intent(this, BudgetDataHolderActivity::class.java).also { startActivity(it) }
            }}
            guestImageButton.setOnClickListener {
                if (eventRecyclerView.adapter?.itemCount==0){
                    val eventAdding = EventAdding(this, supportFragmentManager, null)
                    eventAdding.show()
                    Toast.makeText(this,"Create A Event Before Moving Futher",Toast.LENGTH_SHORT).show()
                }else{
                Intent(this, GuestDataHolderActivity::class.java).also { startActivity(it) }
            }}
            vendorImageButton.setOnClickListener {
                if (eventRecyclerView.adapter?.itemCount==0){
                    val eventAdding = EventAdding(this, supportFragmentManager, null)
                    eventAdding.show()
                    Toast.makeText(this,"Create A Event Before Moving Futher",Toast.LENGTH_SHORT).show()
                }else{
                Intent(this, VendorDataHolderActivity::class.java).also { startActivity(it) }
            }}
        }
        Eventshow.setOnClickListener {
            if (eventshowhide.visibility== View.GONE && eventRecyclerView.visibility==View.GONE){
                eventshowhide.visibility=View.VISIBLE
                eventRecyclerView.visibility=View.VISIBLE
                Eventshow.visibility=View.GONE
                eventaddbut.visibility=View.VISIBLE
            }
            else{
                eventshowhide.visibility=View.GONE
                eventRecyclerView.visibility=View.GONE
                eventaddbut.visibility=View.GONE
                Eventshow.visibility=View.VISIBLE
            }
        }
        crossimg.setOnClickListener {
            if (eventshowhide.visibility== View.VISIBLE && eventRecyclerView.visibility==View.VISIBLE){
                eventshowhide.visibility=View.GONE
                eventRecyclerView.visibility=View.GONE
                Eventshow.visibility=View.VISIBLE
                eventaddbut.visibility=View.GONE
            } else{
                eventshowhide.visibility=View.VISIBLE
                eventRecyclerView.visibility=View.VISIBLE
                eventaddbut.visibility=View.VISIBLE
                Eventshow.visibility=View.GONE
            } }
        eventaddbut.setOnClickListener { val eventadding=EventAdding(this,supportFragmentManager,null)
            eventadding.show()
        }

        if (isFirstLaunch(this)) {
            showFirstLaunchDialog()

            setFirstLaunchFlag(this, false)
        }
//        widgetButton.setOnClickListener { addWidgetToHomeScreen() }
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databasename=getSharedPreference(this,"databasename").toString()
                val db = LocalDatabase(this, databasename)
                val eventList = db.getAllEvents()
                val Eventtimer=db.getEventData(1)
                if(Eventtimer!=null){
                    Eventshow.text=Eventtimer.name
                    budgetShowTextView.text=Eventtimer.budget
                    val name=Eventtimer.name
                    val ParentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
                    if(!ParentDirectory.exists()){
                        ParentDirectory.mkdirs()
                    }
                    val EventNameDirectory=File(ParentDirectory,"$name")
                    if(!EventNameDirectory.exists()){
                        EventNameDirectory.mkdirs()
                    }
                    // Calculate remaining time until the event date
                    val eventDate=Eventtimer.Date
                    val eventTime=Eventtimer.time
                    val currentDate = Calendar.getInstance().time
                    val eventDateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).parse("$eventDate $eventTime")
                    val remainingTimeInMillis = eventDateTime.time - currentDate.time
                    // Start the countdown timer
                    countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val days = millisUntilFinished / (24 * 60 * 60 * 1000)
                            val hours = (millisUntilFinished % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
                            val minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000)
                            val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                            val remainingTime = String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds)
                            EventTimerDisplay.text = remainingTime
                        }
                        override fun onFinish() {
                            EventTimerDisplay.text = "Event Started"
                        }
                    }.start()
                }
                else{
//                    Toast.makeText(this,"Event Not Found",Toast.LENGTH_SHORT).show()
                }
                adapter = EventLayoutAdapter(eventList){ position ->
//                    val eventadding=EventAdding(this,supportFragmentManager,position)
//                    eventadding.show()

                    val popup=PopupMenu(this,eventshowhide)
//                    val popup = findViewById<>()
                    popup.menuInflater.inflate(R.menu.popup_menu,popup.menu)

                        popup.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.Event_Pdf->{
                                if(checkPermissions()){
                                    GeneratePDF()
                                }
                                else{
                                    requestPermission()
                                }
                                true
                            }
                            R.id.event_delete->{
//                                val Event_Delete=DeleteEvent(position)
//                                if(Event_Delete){
//                                    Toast.makeText(this,"${Eventtimer!!.name} has been Deleted",Toast.LENGTH_SHORT).show()
//                                }
                            true
                            }
                           else-> return@setOnMenuItemClickListener false
                        }
                    }
                    popup.show()
                }
                eventRecyclerView.adapter = adapter
                eventRecyclerView.layoutManager = LinearLayoutManager(this)
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing=false
            },1)
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.Coral, R.color.Fuchsia, R.color.Indigo)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Lavender_Blush)
        swipeRefreshLayout.setProgressViewOffset(true, 0, 150)

        //Event  Load instant code
        val filter=IntentFilter("com.example.eventmatics.fragments")
        dataAddedReceiver=object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.action=="com.example.eventmatics.fragments")
                    swipeRefreshLayout.isRefreshing=true
                showEventData()
                swipeRefreshLayout.isRefreshing=false
            }
        }
        registerReceiver(dataAddedReceiver, filter)


        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            Handler(Looper.getMainLooper()).postDelayed({
                showEventData()
                swipeRefreshLayout.isRefreshing = false
            },1)
        }
        swipeRefreshLayout.setProgressViewEndTarget(true,200)

        navigationDrawershow()
    }


//    fun DeleteEvent(event:Events){
//
//
//
//    }
fun createDataCell(data: String, font: com.itextpdf.text.Font): PdfPCell {
    val cell = PdfPCell(Paragraph(data, font))
    cell.borderColor = com.itextpdf.text.BaseColor.BLACK
    cell.borderWidth = 1f
    return cell
}
    fun GeneratedTaskPDF(db:LocalDatabase, pdfPath: File){
        val Event_Details=db.getEventData(1)
        val EventName=Event_Details?.name
        val Task_Details=db.getAllTasks()
        val pdffirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!pdffirectory.exists()){
            pdffirectory.mkdirs()
        }
        val ChildDirectory=File(pdffirectory,"$EventName")
        if(!ChildDirectory.exists()){
            ChildDirectory.mkdirs()
        }
        val pdfFilePath=File(ChildDirectory,"TaskReport.pdf")
        try {
            pdfFilePath.createNewFile()
            val document=Document(PageSize.A4)
            val pdfWriter= PdfWriter.getInstance(document,FileOutputStream(pdfFilePath))
            document.open()
            val TaskTitle=com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,20f,com.itextpdf.text.Font.BOLD)
            val dataFont=com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,12f)

            val title=Paragraph("Task Details",TaskTitle)
            title.alignment=Element.ALIGN_CENTER
            document.add(title)

            val Taskcolumn= listOf("No","Name","Task Category","Note","Task Status","Task Date")
            val TaskColumnSize=Taskcolumn.size

            val table=PdfPTable(TaskColumnSize)
            table.widthPercentage=100f

            for(column in Taskcolumn){
                val cell=PdfPCell(Paragraph(column,dataFont))
                cell.borderColor=com.itextpdf.text.BaseColor.BLACK
                cell.borderWidth=1f
                table.addCell(cell)
            }
            for(task in Task_Details){
                table.addCell(createDataCell(task.id.toString(),dataFont))
                table.addCell(createDataCell(task.taskName,dataFont))
                table.addCell(createDataCell(task.category,dataFont))
                table.addCell(createDataCell(task.taskNote,dataFont))
                table.addCell(createDataCell(task.taskStatus,dataFont))
                table.addCell(createDataCell(task.taskDate,dataFont))
            }
            document.add(table)
            document.close()
            Toast.makeText(this, "PDF file generated successfully", Toast.LENGTH_SHORT).show()

        }catch (e:Exception){
            e.printStackTrace()
            Log.e("PDF", "Failed to generate PDF file: ${e.message}")        }
    }
    fun GenerateBudgetPDF(db:LocalDatabase, pdfPath: File){
        val Budget_Details=db.getAllBudgets()
        val Event_Details=db.getEventData(1)
        val EventName=Event_Details?.name

        val ParentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!ParentDirectory.exists()){
            ParentDirectory.mkdirs()
        }
        val EventDirectroy=File(ParentDirectory,"$EventName")
        if(!EventDirectroy.exists()){
            EventDirectroy.mkdirs()
        }
        val PdfFilePath=File(EventDirectroy,"BudgetReport.pdf")

            try {
                PdfFilePath.createNewFile()

                val document = Document(PageSize.A4)
                val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(PdfFilePath))
                document.open()

                val titleFont = com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 20f, com.itextpdf.text.Font.BOLD)
                val dataFont = com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12f)

                val title = Paragraph("Budget Report", titleFont)
                title.alignment = Element.ALIGN_CENTER
                document.add(title)

                val columnNames = listOf("No", "Name", "Category", "Note", "Estimated")
                val numColumns = columnNames.size

                val table = PdfPTable(numColumns)
                table.widthPercentage = 100f

                for (columnName in columnNames) {
                    val cell = PdfPCell(Paragraph(columnName, dataFont))
                    cell.borderColor = com.itextpdf.text.BaseColor.BLACK
                    cell.borderWidth = 1f
                    table.addCell(cell)
                }


                for (budget in Budget_Details) {
                    table.addCell(createDataCell(budget.id.toString(), dataFont))
                    table.addCell(createDataCell(budget.name, dataFont))
                    table.addCell(createDataCell(budget.category, dataFont))
                    table.addCell(createDataCell(budget.note, dataFont))
                    table.addCell(createDataCell(budget.estimatedAmount, dataFont))
                }

                document.add(table)
                document.close()
                Toast.makeText(this, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("PDF", "Failed to generate PDF file: ${e.message}")
            }
    }
    fun GenerateGuestPDF(db:LocalDatabase, pdfPath: File){
        val Guest_Details=db.getAllGuests()
        val Event_Details=db.getEventData(1)
        val Event_Name=Event_Details?.name
        val ParentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!ParentDirectory.exists()){
            ParentDirectory.mkdirs()
        }
        val EventDirectroy=File(ParentDirectory,"$Event_Name")
        if(!EventDirectroy.exists()){
            EventDirectroy.mkdirs()
        }
        val PdfFilePath=File(EventDirectroy,"GuestReport.pdf")
        try{
            PdfFilePath.createNewFile()
            val document=Document(PageSize.A4)
            val pdfWriter=PdfWriter.getInstance(document,FileOutputStream(PdfFilePath))
            document.open()

            val GuestTitle=com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,20f,com.itextpdf.text.Font.BOLD)
            val datafont=com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,12f)

            val Title=Paragraph("Guest Details",GuestTitle)
            Title.alignment=Element.ALIGN_CENTER
            document.add(Title)

            val GuestColumnName = listOf(
        "No", "Family Name", "Total Member", "Note",
        "Invitation", "Phone Number", "Address")
            val GuestColumnSize=GuestColumnName.size

            val table=PdfPTable(GuestColumnSize)
            table.widthPercentage=100f
            for(column in GuestColumnName){
                val cell=PdfPCell(Paragraph(column,datafont))
                cell.borderColor=com.itextpdf.text.BaseColor.BLACK
                cell.borderWidth=1f
                table.addCell(cell)
            }
            for(Guest in Guest_Details){
                table.addCell(createDataCell(Guest.id.toString(),datafont))
                table.addCell(createDataCell(Guest.name,datafont))
                table.addCell(createDataCell(Guest.totalFamilyMembers,datafont))
                table.addCell(createDataCell(Guest.note,datafont))
                table.addCell(createDataCell(Guest.isInvitationSent,datafont))
                table.addCell(createDataCell(Guest.phoneNumber,datafont))
                table.addCell(createDataCell(Guest.address,datafont))
            }
            document.add(table)
            document.close()
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("PDF","File Failed To Generate ${e.message}")
        }
    }
    fun GeneratedVendorPDF(db: LocalDatabase, pdfPath: File){
        val Vendor_Details=db.getAllVendors()
        val Event_Details=db.getEventData(1)
        val Event_Name=Event_Details?.name
        val ParentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!ParentDirectory.exists()){
            ParentDirectory.mkdirs()
        }
        val EventDirectroy=File(ParentDirectory,"$Event_Name")
        if(!EventDirectroy.exists()){
            EventDirectroy.mkdirs()
        }
        val PdfFilePath=File(EventDirectroy,"VendorReport.pdf")
        try {
            PdfFilePath.createNewFile()
            val document=Document(PageSize.A4)
            val pdfWriter=PdfWriter.getInstance(document,FileOutputStream(PdfFilePath))
            document.open()

            val VendorTitle=com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,20f,com.itextpdf.text.Font.BOLD)
            val dataFont=com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN,15f)

            val Title=Paragraph("Vendor Details",VendorTitle)
            Title.alignment=Element.ALIGN_CENTER
            document.add(Title)

            val VendorcolumnNames = listOf(
        "No","Name", "Category", "Estimated Amount", "Balance",
        "Notes", "Phone Number",
        "Email", "Website", "Address")
            val VendorColumnSize=VendorcolumnNames.size

            val table=PdfPTable(VendorColumnSize)
            table.widthPercentage=100f

            for(column in VendorcolumnNames){
                val cell=PdfPCell(Paragraph(column,dataFont))
                cell.borderColor=com.itextpdf.text.BaseColor.BLACK
                cell.borderWidth=1f
                table.addCell(cell)
            }
            for (vendor in Vendor_Details){
                table.addCell(createDataCell(vendor.id.toString(),dataFont))
                table.addCell(createDataCell(vendor.name,dataFont))
                table.addCell(createDataCell(vendor.name,dataFont))
                table.addCell(createDataCell(vendor.estimatedAmount,dataFont))
                table.addCell(createDataCell(vendor.balance,dataFont))
                table.addCell(createDataCell(vendor.note,dataFont))
                table.addCell(createDataCell(vendor.phonenumber,dataFont))
                table.addCell(createDataCell(vendor.emailid,dataFont))
                table.addCell(createDataCell(vendor.website,dataFont))
                table.addCell(createDataCell(vendor.address,dataFont))
            }
            document.add(table)
            document.close()

        }catch (e:Exception){
            e.printStackTrace()
            Log.e("PDF", "Failed to generate PDF file: ${e.message}")
        }

    }
    fun GeneratePDF() {
        val db = LocalDatabase(this, getSharedPreference(this, "databasename").toString())
        val eventDetails = db.getEventData(1)
        val eventName = eventDetails?.name

        val ParentDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        if (!ParentDirectory.exists()) {
            ParentDirectory.mkdirs()
        }
        val EventDirectroy=File(ParentDirectory,"$eventName")
        if(!EventDirectroy.exists()){
            EventDirectroy.mkdirs()
        }
        val mergedPdfPath = File(EventDirectroy, "MergedReport.pdf").absolutePath

        GenerateGuestPDF(db, EventDirectroy)
        GeneratedVendorPDF(db, EventDirectroy)
        GeneratedTaskPDF(db, EventDirectroy)
        GenerateBudgetPDF(db, EventDirectroy)

        try {
            mergePDFs(mergedPdfPath,
                File(EventDirectroy, "GuestReport.pdf").absolutePath,
                File(EventDirectroy, "VendorReport.pdf").absolutePath,
                File(EventDirectroy, "TaskReport.pdf").absolutePath,
                File(EventDirectroy, "BudgetReport.pdf").absolutePath
            )
            Toast.makeText(this, "PDF files merged successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PDF", "Failed to merge PDFs: ${e.message}")
        }
    }

    fun mergePDFs(outputFilePath: String, vararg pdfFilePaths: String) {
        val document = Document()

        val pdfCopy = PdfCopy(document, FileOutputStream(outputFilePath))
        document.open()
        for (pdfFilePath in pdfFilePaths) {
            val pdfReader = PdfReader(pdfFilePath)
            for (pageIndex in 1..pdfReader.numberOfPages) {
                pdfCopy.addPage(pdfCopy.getImportedPage(pdfReader, pageIndex))
            }
            pdfCopy.freeReader(pdfReader)
            pdfReader.close()
        }

        document.close()
    }


    //fun GeneratePDF(){
//    val databasename=getSharedPreference(this,"databasename").toString()
//    val db=LocalDatabase(this,databasename)
//    val Event_Details=db.getEventData(1)
//    val Task_Details=db.getAllTasks()
//    val Budget_Details=db.getAllBudgets()
//    val Guest_Details=db.getAllGuests()
//    val Vendor_Details=db.getAllVendors()
//    val maxCharsPerCell = 15
//
//    val pagewidth=900
//    val pageheight=1120
//
//    bmp=BitmapFactory.decodeResource(resources,R.drawable.logo)
//    scalebmp=Bitmap.createScaledBitmap(bmp,190,190,false)
//
//    val PDF=PdfDocument()
//
//    val titleBudget= Paint()
//    val header=Paint()
//
//    val Budgetpageinfo:PdfDocument.PageInfo=PdfDocument.PageInfo.Builder(pagewidth,pageheight,2).create()
//    val pageBudget:PdfDocument.Page=PDF.startPage(Budgetpageinfo)
//    val canvasBudget:Canvas=pageBudget.canvas
//    canvasBudget.drawBitmap(scalebmp,20f,20F, Paint())
//
//    titleBudget.textSize=20F
//    titleBudget.color=ContextCompat.getColor(this,R.color.black)
//    canvasBudget.drawText("Budget Report",209F,50F,titleBudget)
//
//    val starty=200F
//    val lineheight=50F
//    val columnNames = listOf("No", "Name", "Category", "Note", "Estimated")
//
//    val borderpaint=Paint()
//    borderpaint.style=Paint.Style.STROKE
//    borderpaint.color=Color.BLACK
//    borderpaint.strokeWidth=2F
//
//    val budgetheader=Paint()
//    budgetheader.textSize=20F
//    budgetheader.color=Color.BLACK
//    budgetheader.textAlign=Paint.Align.CENTER
//
//    val headerX=pagewidth.toFloat()/2
//    val headerY=starty
//    for ((columnIndex, columnName) in columnNames.withIndex()) {
//        val cellWidth = pagewidth / columnNames.size
//        val cellHeight = lineheight
//
//        val cellX = columnIndex * cellWidth
//        val cellY = headerY - cellHeight / 2
//
//        // Draw borders around header cells
//        canvasBudget.drawRect(
//            cellX.toFloat(), cellY, (cellX + cellWidth).toFloat(),
//            (cellY + cellHeight), borderpaint
//        )
//
//        // Draw header text
//        canvasBudget.drawText(columnName,
//            (cellX + cellWidth / 2).toFloat(), cellY + cellHeight / 2, budgetheader)
//    }
//    val dataPaint = Paint()
//    dataPaint.textSize = 12F
//    dataPaint.color = Color.BLACK
//    dataPaint.textAlign = Paint.Align.CENTER
//
//    val cellWidth = pagewidth / columnNames.size
//    val cellHeight = lineheight
//
//    for ((rowIndex, Budget) in Budget_Details.withIndex()) {
//        val xPosition = 0
//        val yPosition = starty + (rowIndex + 1) * lineheight
//
//        val dataItems = listOf(
//            Budget.id.toString(),
//            Budget.name,
//            Budget.category,
//            Budget.note,
//            Budget.estimatedAmount
//
//        )
//        for ((columnIndex, data) in dataItems.withIndex()) {
//            val cellX = columnIndex * cellWidth
//            val cellY = yPosition - cellHeight / 2
//
//            // Draw borders around data cells
//            canvasBudget.drawRect(
//                cellX.toFloat(), cellY, (cellX + cellWidth).toFloat(),
//                (cellY + cellHeight), borderpaint
//            )
//            // Draw data text
//            canvasBudget.drawText(data,
//                (cellX + cellWidth / 2).toFloat(), cellY + cellHeight / 2, dataPaint)
//        }
//    }
//    PDF.finishPage(pageBudget)
//
//    // Task Report Page
//    val pageInfoTask = PdfDocument.PageInfo.Builder(pagewidth, pageheight, 1).create()
//    val pageTask = PDF.startPage(pageInfoTask)
//    val canvasTask = pageTask.canvas
//
//    canvasTask.drawBitmap(scalebmp, 20F, 0F, Paint())
//    val titleTask = Paint()
//    titleTask.textSize = 30F
//    titleTask.color = ContextCompat.getColor(this, R.color.black)
//    canvasTask.drawText("Task Report", 209F, 50F, titleTask)
//    val Taskstarty=200F
//
////        val column= listOf("Family Name","No.of Family Member","NOte","Invitation Status"
////        ,"Acceptance Status","Phone Number(Head of The Family)","Address")
//    val column= listOf("No","Name","Task Category","Note"
//        ,"Task Status","Task Date")
//
//    val borderPaint=Paint()
//    borderPaint.style = Paint.Style.STROKE
//    borderPaint.color=Color.BLACK
//    borderPaint.strokeWidth=2f
//
//    val headerPaint = Paint()
//    headerPaint.textSize = 20F
//    headerPaint.color = Color.BLACK
//    headerPaint.textAlign = Paint.Align.CENTER
//    val HeaderX=pagewidth.toFloat()/2
//    val TaskheaderY=Taskstarty
//
//    for((columnIndex,columns) in column.withIndex()){
//        val cellwidth=pagewidth/column.size
//        val cellheight=lineheight
//
//        val cellX=columnIndex*cellwidth
//        val cellY=headerY-cellheight/2
//
//        canvasTask.drawRect(
//            cellX.toFloat(),cellY,(cellX+cellwidth).toFloat(),(cellY+cellheight),borderPaint
//        )
//        canvasTask.drawText(columns,(cellX+cellwidth/2).toFloat(),cellY+cellheight/2,headerPaint)
//    }
//
//    val TaskdataPaint=Paint()
//    TaskdataPaint.textSize=12F
//    TaskdataPaint.color=Color.BLACK
//    TaskdataPaint.textAlign=Paint.Align.CENTER
//
//    val cellwidth=pagewidth/column.size
//    val cellheight=lineheight
//
//    for((index,Task) in Task_Details.withIndex()){
//        val xpos=0
//        val ypos=Taskstarty+(index+1)*lineheight
//
//        val dataitems= listOf(
//            Task.id.toString(),
//            Task.taskName,
//            Task.category,
//            Task.taskNote,
//            Task.taskStatus,
//            Task.taskDate
//        )
//        for((index,data) in dataitems.withIndex()){
//            val cellX=index*cellwidth
//            val cellY=ypos-cellheight/2
//
//            canvasTask.drawRect(
//                cellX.toFloat(),cellY,(cellX+cellwidth).toFloat(),(cellY+cellheight),borderPaint
//            )
//            canvasTask.drawText(data,(cellX+cellwidth/2).toFloat(),cellY+cellheight/2,dataPaint)
//        }
//    }
//    PDF.finishPage(pageTask)
//
//    val GuestTitle=Paint()
////    val Header=Paint()
//
//    val GuestPageinfo:PdfDocument.PageInfo=PdfDocument.PageInfo.Builder(pagewidth,pageheight,3).create()
//    val GuestPage:PdfDocument.Page=PDF.startPage(GuestPageinfo)
//
//    val GuestCanvas=GuestPage.canvas
//    GuestCanvas.drawBitmap(scalebmp,209F,50F,Paint())
//
//    val startY=200F
//
//    val GuestColumnName = listOf(
//        "No", "Family Name", "Total Member", "Note",
//        "Invitation", "Phone Number", "Address"
//    )
//    GuestTitle.textSize=20F
//    GuestTitle.color=ContextCompat.getColor(this,R.color.black)
//    GuestTitle.textAlign=Paint.Align.CENTER
//
//    val GuestHeader=Paint()
//    GuestHeader.textSize=20F
//    GuestHeader.color=Color.BLACK
//    GuestHeader.textAlign=Paint.Align.CENTER
//
//    for((columnIndex,columns) in GuestColumnName.withIndex() ){
////These lines calculate the width and height of a cell
//        // based on the page width and the number of columns.
//        val cellwidth=pagewidth/GuestColumnName.size
//        val cellheight=lineheight
//
//        //These lines calculate the X and Y coordinates for the top-left corner of the cell being drawn.
//        val cellX=columnIndex*cellwidth
//        val cellY=headerY-cellheight/2
//
//        GuestCanvas.drawRect(
//            cellX.toFloat(), cellY, (cellX + cellwidth).toFloat(),
//            (cellY + cellheight), borderpaint
//        )
//        GuestCanvas.drawText(
//            columns,
//            (cellX + cellWidth / 2).toFloat(), cellY + cellHeight / 2, GuestHeader)
//
//    }
////    These lines calculate the width and height of a cell
////    for the data rows based on the page width and the number of columns.
//    val GuestcellWidth=pagewidth/GuestColumnName.size
//    val Guestcellheight=lineheight
//
//    for((rowIndex,GuestColumn) in Guest_Details.withIndex()){
////        These lines calculate the X and Y coordinates for positioning the current data row.
//        val xposition=0
//        val yPosition=startY+(rowIndex+1)*lineheight
//
//        val dataitem= listOf(
//            GuestColumn.id.toString(),
//            GuestColumn.name,
//            GuestColumn.totalFamilyMembers,
//            GuestColumn.note,
//            GuestColumn.isInvitationSent,
//            GuestColumn.phoneNumber,
//            GuestColumn.address
//        )
//        for((columnIndex,data) in dataitem.withIndex()){
////            These lines calculate the X and Y coordinates for the top-left corner of the cell being drawn.
//            val cellX = columnIndex * GuestcellWidth
//            val cellY = yPosition - Guestcellheight / 2
//
//            GuestCanvas.drawRect(
//                cellX.toFloat(), cellY, (cellX + GuestcellWidth).toFloat(),
//                (cellY + Guestcellheight), borderpaint
//            )
//
//            GuestCanvas.drawText(
//                data,
//                (cellX + GuestcellWidth / 2).toFloat(), cellY + Guestcellheight / 2, dataPaint
//            )
//        }
//
//    }
//        PDF.finishPage(GuestPage)
//
//    val VendorTitle = Paint()
//
//    // Vendor Page
//    val VendorPageInfo = PdfDocument.PageInfo.Builder(pagewidth, pageheight, 4).create()
//    val VendorPage = PDF.startPage(VendorPageInfo)
//
//    val VendorCanvas = VendorPage.canvas
//    val VendorcolumnNames = listOf(
//        "No","Name", "Category", "Estimated Amount", "Balance",
//        "Notes", "Phone Number",
//        "Email", "Website", "Address"
//    )
//    VendorTitle.textSize = 20F
//    VendorTitle.color = ContextCompat.getColor(this, R.color.black)
//    VendorTitle.textAlign = Paint.Align.CENTER
//
//    val VendorHeader = Paint()
//    VendorHeader.textSize = 20F
//    VendorHeader.color = ContextCompat.getColor(this, R.color.black)
//    VendorHeader.textAlign = Paint.Align.CENTER
//
//    for ((columnIndex, ColumnName) in VendorcolumnNames.withIndex()) {
//        val cellwidth = pagewidth / VendorcolumnNames.size
//        val cellheight = lineheight
//
//        val cellX = columnIndex * cellWidth
//        val cellY = headerY - cellheight / 2
//
//        VendorCanvas.drawRect(
//            cellX.toFloat(), cellY, (cellX + cellwidth).toFloat(),
//            (cellY + cellheight), borderpaint
//        )
//        VendorCanvas.drawText(
//            ColumnName,
//            (cellX + cellWidth / 2).toFloat(), cellY + cellHeight / 2, VendorHeader
//        )
//    }
//
//    val VendorCellWidth = pagewidth / VendorcolumnNames.size
//    val VendorCellHeight = lineheight
//
//    for ((rowIndex, Data) in Vendor_Details.withIndex()) {
//        val xposition = 0
//        val yPosition = startY + (rowIndex + 1) * lineheight
//
//        val VendorDataItem = listOf(
//            Data.id.toString(),
//            Data.name,
//            Data.category,
//            Data.estimatedAmount,
//            Data.balance,
//            Data.phonenumber,
//            Data.emailid,
//            Data.website,
//            Data.address
//        )
//        for ((ColumnIndex, VendorData) in VendorDataItem.withIndex()) {
//            val cellX = ColumnIndex * VendorCellWidth
//            val cellY = yPosition - VendorCellHeight / 2
//            val wrappedText = wrapText(VendorData, maxCharsPerCell)
//            VendorCanvas.drawRect(
//                cellX.toFloat(), cellY, (cellX + VendorCellWidth).toFloat(),
//                (cellY + VendorCellHeight), borderpaint
//            )
//
//            // Draw wrapped text in the cell
//            val textLines = wrappedText.split("\n")
//            val lineHeight = 12 // Adjust the line height as needed
//            var textY = cellY + VendorCellHeight / 2 - (textLines.size * lineHeight / 2)
//            for (line in textLines) {
//                VendorCanvas.drawText(
//                    line,
//                    (cellX + VendorCellWidth / 2).toFloat(), textY.toFloat(), dataPaint
//                )
//                textY += lineHeight
//            }
//        }
//    }
//
//    PDF.finishPage(VendorPage)
//
//    val pdfDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
//    if (!pdfDirectory.exists()) {
//        pdfDirectory.mkdirs()
//    }
//    val name=db.getEventData(1)
//    val eventName=name?.name
//    val eventDirectory = File(pdfDirectory, "HH")
//    if (!eventDirectory.exists()) {
//        eventDirectory.mkdirs()
//    }
//    val pdfFilePath = File(eventDirectory, "CombinedReport.pdf")
//
//    try {
//        pdfFilePath.createNewFile()
//        PDF.writeTo(FileOutputStream(pdfFilePath))
//        Toast.makeText(this, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
//        Log.d("PDF", "PDF file generated successfully.")
//    } catch (e: Exception) {
//        e.printStackTrace()
//        Log.e("PDF", "Failed to generate PDF file: ${e.message}")
//    }
//
//    PDF.close()
//}
    private fun wrapText(text: String, maxLength: Int): String {
        val regex = "(.{$maxLength})"
        return text.replace(regex.toRegex(), "$1\n")
    }
fun checkPermissions():Boolean{
    val writepermission=ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
    val readpermission=ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
    return writepermission==PackageManager.PERMISSION_GRANTED && readpermission==PackageManager.PERMISSION_GRANTED

}
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE
        ),PERMISSION_CODE)
    }
//    private fun launchNewActivity(selectedItem: String) {
//        val intent = Intent(this, EventAdding::class.java)
//        intent.putExtra("SELECTED_ITEM", selectedItem)
//        startActivity(intent)
//    }

    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
    private fun showFirstLaunchDialog() {
        val eventAdding=EventAdding(this,supportFragmentManager,null)
        eventAdding.show()
    }
    fun isFirstLaunch(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }
    fun setFirstLaunchFlag(context: Context, isFirstLaunch: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean("isFirstLaunch", isFirstLaunch).apply()
    }

    override fun onResume() {
        super.onResume()
        showEventData()
    }
    @SuppressLint("Range")
     fun showEventData() {
        val databasename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databasename)
        val eventList = db.getAllEvents()
        val Eventtimer = db.getEventData(1)
        val budgettotdal=db.getTotalBudget()
        val budgetUnPaid=db.getTotalUnPaid()
        if (Eventtimer != null) {
            Eventshow.text = Eventtimer.name
            budgetShowTextView.text = Eventtimer.budget
//            val name=Eventtimer.name
//            val ParentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
//            if(!ParentDirectory.exists()){
//                ParentDirectory.mkdirs()
//            }
//            val EventNameDirectory=File(ParentDirectory,name)
//            if(!EventNameDirectory.exists()){
//                EventNameDirectory.mkdirs()
//            }
//            Intent().also {
//                it.putExtra("ChildDirectory",EventNameDirectory.absolutePath)
//            }
            val budget=Eventtimer.budget
            piechart.clearChart()
            piechart.addPieSlice(PieModel("Event",budget.toFloat(),Color.parseColor("#2ecc71")))
            piechart.addPieSlice(PieModel("Paid",budgettotdal.toFloat(),Color.parseColor("#3498db")))
            piechart.addPieSlice(PieModel("UnPaid",budgetUnPaid.toFloat(),Color.parseColor("#e74c3c")))
            piechart.startAnimation()
            paidAmountShowTextView.setText(budgettotdal.toString())
            pendingAmountShowTextView.setText(budgetUnPaid.toString())

            // Calculate remaining time until the event date
            val eventDate = Eventtimer.Date
            val eventTime = Eventtimer.time
            val currentDate = Calendar.getInstance().time
            val eventDateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).parse("$eventDate $eventTime")

            if (eventDateTime != null) {
                val remainingTimeInMillis = eventDateTime.time - currentDate.time

                // Start the countdown timer
                countDownTimer?.cancel() // Cancel any existing timer to avoid overlapping
                countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val days = millisUntilFinished / (24 * 60 * 60 * 1000)
                        val hours = (millisUntilFinished % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
                        val minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000)
                        val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                        val remainingTime = String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds)
                        EventTimerDisplay.text = remainingTime
                    }

                    override fun onFinish() {
                        EventTimerDisplay.text = "Event Started"
                    }
                }.start()
            } else {
                Log.e("CountdownError", "Error parsing event date and time.")
            }
        } else {
//            Toast.makeText(this, "Event Not Found", Toast.LENGTH_SHORT).show()
        }

        val adapter = EventLayoutAdapter(eventList){ position ->
//            val eventadding=EventAdding(this,supportFragmentManager,position)
//            eventadding.show()
            val popup=PopupMenu(this,eventRecyclerView)
            popup.menuInflater.inflate(R.menu.popup_menu,popup.menu)

            popup.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.Event_Pdf->{
                        if(checkPermissions()){
                            GeneratePDF()
                        }
                        else{
                            requestPermission()
                        }
                        true
                    }
                    R.id.event_delete->{
//                        val Event_Delete=DeleteEvent(position)
//                        if(Event_Delete){
//                            Toast.makeText(this,"${Eventtimer!!.name} has been Deleted",Toast.LENGTH_SHORT).show()
//                        }
                        val eventtodelete=eventList[position]
                        val rowaffected=DeleteEvent(eventtodelete)
                        try {

                        if(rowaffected){
                            eventList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(this, "${eventtodelete.name} has been Deleted", Toast.LENGTH_SHORT).show()
                            Log.d("Delete","Item Deleted")
                        }
                        }
                        catch (e:Exception){
                            Log.d("Delete","${e.message}")

                        }
                        true
                    }
                    else-> return@setOnMenuItemClickListener false
                }
            }
            popup.show()
        }
        eventRecyclerView.adapter = adapter
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

    private fun DeleteEvent(event:Events):Boolean {
        val databasename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databasename)
        db.deleteEvent(event)
        Toast.makeText(this,"Event Removed",Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }
    private fun addWidgetToHomeScreen() {
        val context = applicationContext
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val myWidgetProvider = ComponentName(context, EventWidget::class.java)

        // Check if the widget is already added
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            val successCallback = PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, EventWidget::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            appWidgetManager.requestPinAppWidget(myWidgetProvider, null, successCallback)
        } else {
        }
    }


    //Navigation Drawer function
    private fun navigationDrawershow() {

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                android.R.id.home -> {
                    // Open the drawer when the navigation button is clicked
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_whatsapp->{
                    sendWhatsAppMessage()
                    true
                }
                R.id.nav_telegram->{
                    val telegramUsername = "@Anuragdevloper"
                    val telegramUri = Uri.parse("https://t.me/$telegramUsername")
                    val telegramIntent = Intent(Intent.ACTION_VIEW, telegramUri)
                    startActivity(telegramIntent)
                    true
                }
                R.id.nav_settings->{
                    Intent(this,SettingActivity::class.java).also { startActivity(it) }
                    true
                }
                R.id.nav_pdf->{
                    Intent(this,PDF_Report::class.java).also { startActivity(it) }

                    true
                }
                R.id.nav_logout->{
                   userlogout()
                    true
                }
                R.id.nav_manage_event->{
                    val EventName=DatabaseNameHolder(this)
                    EventName.show()
                    true
                }
                R.id.widget->{
                    addWidgetToHomeScreen()
                    true
                }
                else -> false
            }
        }
       drawerLayout.addDrawerListener(object :DrawerLayout.SimpleDrawerListener(){
           override fun onDrawerOpened(drawerView: View) {
               swipeRefreshLayout.isEnabled=false
           }

           override fun onDrawerClosed(drawerView: View) {
               super.onDrawerClosed(drawerView)
               swipeRefreshLayout.isEnabled=true

           }

       })
}

    private fun userlogout() {
        val googleSIgnInClient= GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSIgnInClient.signOut().addOnCompleteListener (this){task->
            if (task.isSuccessful){
                Intent(this,signin_account::class.java).also { startActivity(it) }
                finish()
                Toast.makeText(this,"You Have been Logout Successfully",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser!=null){
            FirebaseAuth.getInstance().signOut()
            Intent(this,signin_account::class.java).also { startActivity(it) }
            finish()
            Toast.makeText(this,"You Have been Logout Successfully",Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendWhatsAppMessage() {
        // Phone number of the recipient
        val phoneNumber = "9004040592"

        // Message to be sent
        val message = "Hello, I Need your Help"

        // Get the package manager
        val packageManager = packageManager

        // Create an intent with the ACTION_VIEW action
        val whatsappIntent = Intent(Intent.ACTION_VIEW)

        // Construct the WhatsApp URL with the phone number and message
        val whatsappUrl = "https://api.whatsapp.com/send?phone=+91$phoneNumber&text=$message"

        // Set the data URI of the intent to the WhatsApp URL
        whatsappIntent.data = Uri.parse(whatsappUrl)

        // Check if there is an app installed that can handle the intent
        if (whatsappIntent.resolveActivity(packageManager) != null) {
            startActivity(whatsappIntent)
        } else {
            Toast.makeText(this, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Unregister the broadcast receiver to avoid memory leaks
        unregisterReceiver(dataAddedReceiver)
    }
}
