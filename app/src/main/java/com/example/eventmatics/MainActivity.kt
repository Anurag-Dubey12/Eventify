@file:Suppress("DEPRECATION")

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
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventLayoutAdapter
import com.example.eventmatics.Events_Data_Holder_Activity.BudgetDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.GuestDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.TaskDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.VendorDataHolderActivity
import com.example.eventmatics.Login_Activity.Login_SignUp_Option
import com.example.eventmatics.Login_Activity.signin_account
import com.example.eventmatics.NavigationDrawer.About
import com.example.eventmatics.NavigationDrawer.EventList
import com.example.eventmatics.NavigationDrawer.SettingActivity
import com.example.eventmatics.SQLiteDatabase.Dataclass.AuthenticationUid
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfile
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfileDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager.saveToSharedPreferences
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.DatabaseNameDataClass
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Events
import com.example.eventmatics.fragments.EventAdding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfWriter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var countDownTimer:CountDownTimer?=null
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var taskImageButton: ImageButton
    private lateinit var budgetImageButton: ImageButton
    private lateinit var imageadd: CircleImageView
    private lateinit var guestImageButton: ImageButton
    private lateinit var vendorImageButton: ImageButton
    private lateinit var budgetInfoCardView: CardView
    private lateinit var budgetShowTextView: TextView
    private lateinit var vendorTotalAmount: TextView
    private lateinit var taskCompleted: TextView
    private lateinit var vendorPendingAmount: TextView
    private lateinit var vendorPaidAmount: TextView
    private lateinit var taskPending: TextView
    private lateinit var nameadd: TextView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var totalTask: TextView
    private lateinit var totalInvi: TextView
    private lateinit var totalInvitationSent: TextView
    private lateinit var totalInvitationNotSent: TextView
    private lateinit var eventshow: MaterialButton
    private lateinit var budgetSummary: TextView
    private lateinit var guestSummary: TextView
    private lateinit var taskSummary: TextView
    private lateinit var vendorSummary: TextView
    private lateinit var eventname: TextView
    private lateinit var eventTimerDisplay: TextView
    private lateinit var eventshowhide: LinearLayout
    private lateinit var eventActivity: LinearLayout
    private lateinit var pendingAmountShowTextView: TextView
    private lateinit var paidAmountShowTextView: TextView
    private lateinit var eventaddbut: MaterialButton
    private lateinit var adapter: EventLayoutAdapter
    private  var eventList:MutableList<Events> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dataAddedReceiver: BroadcastReceiver
    private lateinit var editprofile: MaterialButton
    private lateinit var generatePdf: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var profileDialog: BottomSheetDialog
    private lateinit var imageAddOption: BottomSheetDialog
    private val permissioncode=101
    private val galley_req_code = 201
    private var selectedImageUri:Uri?=null

    //Event ID
    private lateinit var eventName: TextInputEditText
    private lateinit var eventDate: TextInputEditText
    private lateinit var eventTime: TextInputEditText
    private lateinit var eventBudget: TextInputEditText
    private lateinit var createButton: Button
    private lateinit var EditButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawerlayout)
        navView= findViewById(R.id.navView)
        eventRecyclerView = findViewById(R.id.Eventrec)
        taskImageButton = findViewById(R.id.task)
        taskPending = findViewById(R.id.TaskPending)
        vendorPendingAmount = findViewById(R.id.VendorPendingAmount)
        eventname = findViewById(R.id.eventname)
        vendorPaidAmount = findViewById(R.id.VendorPaidAmount)
        totalInvi = findViewById(R.id.TotalInvi)
        budgetSummary = findViewById(R.id.BudgetSummary)
        taskSummary = findViewById(R.id.TaskSummaryText)
        vendorSummary = findViewById(R.id.VendorSummary)
        guestSummary = findViewById(R.id.GuestSummaryText)
        totalInvitationSent = findViewById(R.id.TotalInviSent)
        totalInvitationNotSent = findViewById(R.id.TotalInvinotSent)
        eventActivity = findViewById(R.id.eventActivity)
        vendorTotalAmount = findViewById(R.id.VendorTotalAmount)
        taskCompleted = findViewById(R.id.TaskCompleted)
        generatePdf = findViewById(R.id.GeneratePdf)
        totalTask = findViewById(R.id.TotalTask)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
        eventTimerDisplay = findViewById(R.id.EventTimerDisplay)
        eventaddbut = findViewById(R.id.eventaddbut)
        budgetImageButton = findViewById(R.id.budget)
        eventshowhide = findViewById(R.id.eventshowhide)
        guestImageButton = findViewById(R.id.Guest)
        vendorImageButton = findViewById(R.id.Vendor)
        eventshow = findViewById(R.id.eventnameshow)
        budgetInfoCardView = findViewById(R.id.budget_info)
        budgetShowTextView = findViewById(R.id.Budgetshow)
        pendingAmountShowTextView = findViewById(R.id.PendingAmountshow)
        paidAmountShowTextView = findViewById(R.id.PaidAmountshow)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        eventList.clear()
        val userUid = AuthenticationUid.getUserUid(this)!!
        Log.d("useruid","The Uid is :$userUid")

        //For First Time Launch Event Create SHow
        if (isFirstLaunch(this)) {
            showFirstLaunchDialog()
            setFirstLaunchFlag(this, false)
        }
        DatabaseManager.initialize(this)
        showEventData()
        //Load Profile Pic
        val db=UserProfileDatabase(this)
        val headerView = navView.getHeaderView(0)
        val userprofile=headerView.findViewById<CircleImageView>(R.id.profilepic)
        val username=headerView.findViewById<TextView>(R.id.UserNameView)
        editprofile=headerView.findViewById(R.id.editProfileButton)
        editprofile.setOnClickListener {
            editprofiledialog()
        }
        val userImagedb=db.getUserProfilebyID(1)
        val userimage=userImagedb?.Image
        if(userimage!=null){
            val image=BitmapFactory.decodeByteArray(userimage,0, userimage!!.size)
            userprofile.setImageBitmap(image)
        }
        val userName=userImagedb?.name
        if(userName!=null){
            username.text= userName.toString()
        }
        if (eventRecyclerView.adapter?.itemCount == 0) {
            eventTimerDisplay.text=" "
            eventname.text=" "
            val eventadding=EventAdding(this,supportFragmentManager,null)
            eventadding.show()
//           showeventfragemnt()
        }
        else {
            guestSummary.setOnClickListener { checkAndStartActivity(GuestDataHolderActivity::class.java) }
            taskSummary.setOnClickListener { checkAndStartActivity(TaskDataHolderActivity::class.java) }
            budgetSummary.setOnClickListener { checkAndStartActivity(BudgetDataHolderActivity::class.java) }
            vendorSummary.setOnClickListener { checkAndStartActivity(VendorDataHolderActivity::class.java) }
            taskImageButton.setOnClickListener { checkAndStartActivity(TaskDataHolderActivity::class.java) }
            budgetImageButton.setOnClickListener { checkAndStartActivity(BudgetDataHolderActivity::class.java) }
            guestImageButton.setOnClickListener { checkAndStartActivity(GuestDataHolderActivity::class.java) }
            vendorImageButton.setOnClickListener { checkAndStartActivity(VendorDataHolderActivity::class.java) }
        }

        eventshow.setOnClickListener {
            val isRecyclerViewVisible = eventRecyclerView.visibility == View.VISIBLE
            val isActivityVisible = eventActivity.visibility == View.VISIBLE
            eventshow.icon = if (isRecyclerViewVisible) getDrawable(R.drawable.show_event) else getDrawable(R.drawable.up_arrow)
            eventRecyclerView.visibility = if (isRecyclerViewVisible) View.GONE else View.VISIBLE
            eventaddbut.visibility = if (isRecyclerViewVisible) View.GONE else View.VISIBLE
            eventActivity.visibility = if (isActivityVisible) View.GONE else View.VISIBLE
        }

        eventaddbut.setOnClickListener {
//            showeventfragemnt()
            val eventadding=EventAdding(this,supportFragmentManager,null)
            eventadding.show()
        }
        generatePdf.setOnClickListener {
            if (eventRecyclerView.adapter?.itemCount == 0) {
                Toast.makeText(this, "Create Event First", Toast.LENGTH_SHORT).show()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle("PDF")
                    .setMessage("Select in Which format you want pdf")
                    .setNeutralButton("Seprate PDF") { _, _ ->
                        handlePdfGeneration(true)
                    }
                    .setPositiveButton("Combined PDF") { _, _ ->
                        handlePdfGeneration(false)
                    }
                    .show()
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
               showEventData()
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

    private fun showeventfragemnt() {
        val dialog=layoutInflater.inflate(R.layout.fragment_event_adding,null)
        eventName = dialog.findViewById(R.id.eventname)!!
        eventDate = dialog.findViewById(R.id.eventdate)!!
        eventTime = dialog.findViewById(R.id.eventtime)!!
        eventBudget = dialog.findViewById(R.id.eventbudget)!!
        createButton = dialog.findViewById(R.id.eventcreatebut)!!
        val view=BottomSheetDialog(this)
        view.setContentView(dialog)
        view.create()
        val today = Calendar.getInstance()
        val todayFormattedDate = "${today.get(Calendar.DAY_OF_MONTH)}/${today.get(Calendar.MONTH) + 1}/${today.get(Calendar.YEAR)}"
        val todayFormattedTime = String.format(
            "%02d:%02d:%02d",
            today.get(Calendar.HOUR_OF_DAY),
            today.get(Calendar.MINUTE),
            today.get(Calendar.SECOND)
        )
        eventDate.setText(todayFormattedDate)
        eventTime.setText(todayFormattedTime)
        eventDate.setOnClickListener { showDatePicker() }
        eventTime.setOnClickListener { showTimePicker() }
        createButton.setOnClickListener {
            val databasename=getSharedPreference(this,"databasename").toString()
            val eventNameText = eventName.text.toString()
            var eventDateText = eventDate.text.toString()
            val eventTimeText = eventTime.text.toString()
            val eventBudgetText = eventBudget.text.toString()

            if (eventNameText.isEmpty()) {
                eventName.error = "Enter Event Name"
                return@setOnClickListener
            }
            if (eventDateText.isEmpty()) {
                eventDateText = "Date is not Defined"
            }
            if (eventBudgetText.isEmpty()) {
                eventBudget.error = "Enter Budget"
                return@setOnClickListener
            }
            if (eventTimeText.isEmpty()) {
                eventTime.error = "Select Time"
                return@setOnClickListener
            }

            DatabaseManager.initialize(this)
            val db=DatabaseManager.getDatabase(this)
            val Databasename=NamesDatabase(this)
            if (db.isEventNameExists(eventNameText)) {
                Toast.makeText(this, "Event name must be unique", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uid=AuthenticationUid.getUserUid(this)!!
            val event = Events(0, eventNameText, eventDateText, eventTimeText, eventBudgetText,uid)
            val names= DatabaseNameDataClass(0,eventNameText,eventDateText,eventTimeText, eventBudgetText,uid)
            Log.d("EventDetails","The Event Details are:$event}")
            Log.d("UerUid","This is EventAdding userid:$names")
            val eventId = db.createEvent(event)
            saveToSharedPreferences(this,"databasename",databasename)
            Databasename.createDatabase(names)
            if (eventId != -1L) {
                swipeRefreshLayout.isRefreshing=true
                Handler().postDelayed({
                   showEventData()
                    swipeRefreshLayout.isRefreshing = false
                }, 1000)
                Toast.makeText(this, "Event created successfully", Toast.LENGTH_SHORT).show()
                view.dismiss()
            } else {
                Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show()
            }
            db.close()
            Databasename.close()
        }
        view.show()
    }
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(minute)
            .setTitleText("Select Event Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, second)
            eventTime.setText(formattedTime)

        }

        timePicker.show(supportFragmentManager, "TAG_TIME_PICKER")
    }
    private fun showDatePicker() {
        val constraintBulder=CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val calendar = Calendar.getInstance()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintBulder.build())
            .build()
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDate
            val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val selectedMonth = selectedCalendar.get(Calendar.MONTH)
            val selectedYear = selectedCalendar.get(Calendar.YEAR)
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            eventDate.setText(formattedDate)
        }
        datePicker.show(supportFragmentManager, "datePicker")

    }
    fun handlePdfGeneration(isSeparatePdf: Boolean) {
        if (checkPermissions()) {
            if (isSeparatePdf) {
                sepratePDF()
            } else {
                generatePDF()
            }
            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }
    }

    private fun editprofiledialog() {
        profileDialog= BottomSheetDialog(this)
        profileDialog.setContentView(R.layout.editprofiledialog)
        profileDialog.show()
        val db=UserProfileDatabase(this)
        imageadd=profileDialog.findViewById(R.id.uploadImage)!!
        nameadd=profileDialog.findViewById(R.id.uploadName)!!
        saveButton=profileDialog.findViewById(R.id.saveButton)!!
        val userData=db.getUserProfilebyID(1)
        val currentname=userData?.name ?:" "
        val userimage=userData?.Image
        try{
        if(userData!=null){
            val image=BitmapFactory.decodeByteArray(userimage,0, userimage!!.size)
            imageadd.setImageBitmap(image)
        }
        imageadd.setOnClickListener {
            imageUploaddialog()
        }
        nameadd.text=currentname
        }catch (e:Exception){
            Log.d("Image","Crash due to:${e.message}")
        }
        saveButton.setOnClickListener {
            if(selectedImageUri!=null){
                val getImageByte=getImageByte(selectedImageUri!!)
                val userid=FirebaseAuth.getInstance().currentUser?.uid
                if(userid!=null){

                  val  username = nameadd.text.toString()
                    val existinguser=db.getUserProfilebyID(1)
                    if(existinguser!=null){
                        val userprofile=UserProfile(1,username,getImageByte)
                        db.updateUserProfile(userprofile)
                        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
                        val image=BitmapFactory.decodeByteArray(getImageByte,0,getImageByte.size)
                        imageadd.setImageBitmap(image)
                        recreate()
                    }
                    else{
                        val userprofile=UserProfile(1,username,getImageByte)
                        db.insertUserProfile(userprofile)
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                        val image=BitmapFactory.decodeByteArray(getImageByte,0,getImageByte.size)
                        imageadd.setImageBitmap(image)
                        recreate()
                    }
                }
            }else{
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
            profileDialog.dismiss()
            drawerLayout.close()
        }
    }
    private fun imageUploaddialog(){
        imageAddOption=BottomSheetDialog(this)
        imageAddOption.setContentView(R.layout.profiledialog)
        imageAddOption.show()
        
        val imageGallery=imageAddOption.findViewById<ImageView>(R.id.ImageGallery)

        imageGallery?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf( android.Manifest.permission.CAMERA),galley_req_code)
            }else{
                Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
                    startActivityForResult(it,galley_req_code)
                    imageAddOption.dismiss()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                galley_req_code->{
                    val selecteddata=data?.data
                    if(selecteddata!=null){
                        selectedImageUri=selecteddata
                        val getimagebyte=getImageByte(selecteddata)
                        val image=BitmapFactory.decodeByteArray(getimagebyte,0,getimagebyte.size)
                        imageadd.setImageBitmap(image)
                    }
                }
            }
        }
    }
    private fun getImageByte(image: Uri):ByteArray{
        val userimage=contentResolver.openInputStream(image)
        return userimage?.readBytes() ?: ByteArray(0)
    }
    private fun setAppTheme() {
        val theme=getThemePreference(this,"Theme")
        AppCompatDelegate.setDefaultNightMode(theme)
    }
    private fun checkAndStartActivity(targetActivity:Class<*>){
        if (eventRecyclerView.adapter?.itemCount==0){
            showeventfragemnt()
            Toast.makeText(this, "Create an Event Before Moving Further", Toast.LENGTH_SHORT).show()
        }
        else{
            Intent(this,targetActivity).also { startActivity(it) }
        }
    }
    private fun createDataCell(data: String, font: Font): PdfPCell {
    val cell = PdfPCell(Paragraph(data, font))
    cell.borderColor = com.itextpdf.text.BaseColor.BLACK
    cell.borderWidth = 1f
    return cell
}
    class HeaderFooterEvent : PdfPageEventHelper() {
        override fun onEndPage(writer: PdfWriter?, document: Document?) {
            val eventifyFont = Font(Font.FontFamily.TIMES_ROMAN, 15f,Font.BOLDITALIC)
            val dateFont = Font(Font.FontFamily.TIMES_ROMAN, 15f)

            val eventifyPhrase = Phrase("Eventify", eventifyFont)
            val datePhrase = Phrase(SimpleDateFormat("dd/MM/yyyy").format(Date()), dateFont)
            val pageFont = Font(Font.FontFamily.TIMES_ROMAN, 10f)

            val centerX = (document!!.left() + document.right()) / 2
            val topY = document.top() + 10f

            ColumnText.showTextAligned(
                writer!!.directContent,
                Element.ALIGN_CENTER,
                eventifyPhrase,
                centerX,
                topY,
                0f
            )

            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_LEFT,
                datePhrase,
                document.left() + 36f,
                document.bottom() - 10f,
                0f
            )

            //Adding page number
            val pageNumberPhrase = Phrase("Page " + writer.pageNumber, pageFont)
            ColumnText.showTextAligned(
                writer.directContent,
                Element.ALIGN_CENTER,
                pageNumberPhrase,
                centerX,
                document.bottom() - 10f,
                0f
            )
        }
    }

    private fun generateTaskPDF(db: LocalDatabase, pdfPath: File) {
        val eventdetails = db.getEventData(1)
        val eventname = eventdetails?.name
        val taskDetails = db.getAllTasks()
        val totaltask=db.getTotalTask()
        val totaltaskcom=db.getTaskStatus()
        val totalpending=db.getTaskPendingStatus()

        val pdffirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        if (!pdffirectory.exists()) {
            pdffirectory.mkdirs()
        }
        val childDirectory = File(pdffirectory, "$eventname")
        if (!childDirectory.exists()) {
            childDirectory.mkdirs()
        }
        val pdfFilePath = File(childDirectory, "TaskReport.pdf")

        try {
            pdfFilePath.createNewFile()
            val document = Document(PageSize.A4)
            val headerFooterEvent = HeaderFooterEvent()
            val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))
            pdfWriter.pageEvent = headerFooterEvent
            document.open()
            val imgFile = R.drawable.app_logo
            val bitmap = BitmapFactory.decodeResource(resources, imgFile)
            val byteArray = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
            val image = Image.getInstance(byteArray.toByteArray())
            image.scaleToFit(100f, 100f)
            image.alignment = Element.ALIGN_LEFT
            document.add(image)
            val taskTitle = com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 20f)
            val dataFont = Font(Font.FontFamily.TIMES_ROMAN, 12f)
            val parafont=Font(Font.FontFamily.TIMES_ROMAN,15f,Font.BOLD)

            val para=Paragraph()
            val paravalue="Total Task:$totaltask \n " +
                    "Total Task Completed:$totaltaskcom \n" +
                    "Total Task Pending:$totalpending"
            val chunk=Chunk(paravalue,parafont)
            para.add(chunk)
            para.alignment=Element.ALIGN_RIGHT
            para.spacingAfter=30f
            val title = Paragraph("Task Details", taskTitle)
            title.alignment = Element.ALIGN_CENTER
            title.spacingAfter=30f
            document.add(title)
            document.add(para)
            val taskcolumn = listOf("No", "Name", "Task Category", "Note", "Task Status", "Task Date")
            val taskcolumnSize = taskcolumn.size

            val table = PdfPTable(taskcolumnSize)
            table.widthPercentage = 100f

            for (column in taskcolumn) {
                val cell = PdfPCell(Paragraph(column, dataFont))
                cell.borderColor = com.itextpdf.text.BaseColor.BLACK
                cell.backgroundColor=com.itextpdf.text.BaseColor.GRAY
                cell.borderWidth = 1f
                table.addCell(cell)
            }
            for (task in taskDetails) {
                table.addCell(createDataCell(task.id.toString(), dataFont))
                table.addCell(createDataCell(task.taskName, dataFont))
                table.addCell(createDataCell(task.category, dataFont))
                table.addCell(createDataCell(task.taskNote, dataFont))
                table.addCell(createDataCell(task.taskStatus, dataFont))
                table.addCell(createDataCell(task.taskDate, dataFont))
            }
            document.add(table)
            document.close()
            Toast.makeText(this, "PDF file generated successfully", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PDF", "Failed to generate PDF file: ${e.message}")
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun generateBudgetPDF(db:LocalDatabase, pdfPath: File){
        val budgetdetails=db.getAllBudgets()
        val eventdetails=db.getEventData(1)
        val eventname=eventdetails?.name
        val totalbudgetnotpaid=db.getTotalNotPaidBudget()
        val totalbudget=db.getTotalBudget()
        val totalbudgetpaid=db.getTotalPaidBudget()
        val parentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!parentDirectory.exists()){
            parentDirectory.mkdirs()
        }
        val eventDirectroy=File(parentDirectory,"$eventname")
        if(!eventDirectroy.exists()){
            eventDirectroy.mkdirs()
        }
        val pdfFilePath=File(eventDirectroy,"BudgetReport.pdf")

            try {
                pdfFilePath.createNewFile()
                val headerFooterEvent=HeaderFooterEvent()
                val document = Document(PageSize.A4)
                val pdfWriter = PdfWriter.getInstance(document, FileOutputStream(pdfFilePath))
                pdfWriter.pageEvent=headerFooterEvent
                document.open()
                val imgFile = R.drawable.app_logo
                val bitmap = BitmapFactory.decodeResource(resources, imgFile)
                val byteArray = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
                val image = Image.getInstance(byteArray.toByteArray())
                image.scaleToFit(100f, 100f)
                image.alignment = Element.ALIGN_LEFT
                document.add(image)
                val titleFont = Font(Font.FontFamily.TIMES_ROMAN, 20f, Font.BOLD)
                val dataFont = Font(Font.FontFamily.TIMES_ROMAN, 12f)
                val parafont=Font(Font.FontFamily.TIMES_ROMAN,15f,Font.BOLD)

                val para=Paragraph()
                val paravalue="Total Budget:$totalbudget \n" +
                        "Total Amount Paid:$totalbudgetpaid \n" +
                        "Total Amount UnPaid:$totalbudgetnotpaid"
                val chunk=Chunk(paravalue,parafont)
                para.add(chunk)
                para.alignment=Element.ALIGN_RIGHT
                para.spacingAfter=30f
                val title = Paragraph("Budget Report", titleFont)
                title.alignment = Element.ALIGN_CENTER
                document.add(title)
                document.add(para)
                val columnNames = listOf("No", "Name", "Category", "Note", "Estimated")
                val numColumns = columnNames.size

                val table = PdfPTable(numColumns)
                table.widthPercentage = 100f

                for (columnName in columnNames) {
                    val cell = PdfPCell(Paragraph(columnName, dataFont))
                    cell.borderColor = com.itextpdf.text.BaseColor.BLACK
                    cell.backgroundColor=com.itextpdf.text.BaseColor.GRAY
                    cell.borderWidth = 1f
                    table.addCell(cell)
                }


                for (budget in budgetdetails) {
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
    private fun generateGuestPDF(db:LocalDatabase, pdfPath: File){
        val guestdetails=db.getAllGuests()
        val totalinvi=db.getTotalInvitation()
        val totalinvinot=db.getTotalInvitationsNotSent()
        val totalinvisnt=db.getTotalInvitationsSent()
        val eventdetails=db.getEventData(1)
        val eventname=eventdetails?.name

        val parentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!parentDirectory.exists()){
            parentDirectory.mkdirs()
        }
        val eventDirectroy=File(parentDirectory,"$eventname")
        if(!eventDirectroy.exists()){
            eventDirectroy.mkdirs()
        }
        val pdfFilePath=File(eventDirectroy,"GuestReport.pdf")
        try{
            pdfFilePath.createNewFile()
            val document=Document(PageSize.A4)
            val headerFooterEvent = HeaderFooterEvent()
            val pdfWriter=PdfWriter.getInstance(document,FileOutputStream(pdfFilePath))
            pdfWriter.pageEvent=headerFooterEvent
            document.open()
            val imgfile=R.drawable.app_logo
            val bitmap=BitmapFactory.decodeResource(resources,imgfile)
            val bytearray=ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytearray)
            val image=Image.getInstance(bytearray.toByteArray())
            image.scaleToFit(100f,100f)
            image.alignment=Element.ALIGN_LEFT
            document.add(image)
            val guestTitle= Font(Font.FontFamily.TIMES_ROMAN,20f, Font.BOLD)
            val datafont= Font(Font.FontFamily.TIMES_ROMAN,12f)
            val parafont=Font(Font.FontFamily.TIMES_ROMAN,15f,Font.BOLD)
            val para=Paragraph()
            val paravalue="Total Invitation :$totalinvi \n" +
                    "Total Invitation Sent:$totalinvisnt \n" +
                    "Total Invitation Not Sent:$totalinvinot"
            val chunk=Chunk(paravalue,parafont)
            para.alignment=Element.ALIGN_RIGHT
            para.spacingAfter=30f
            para.add(chunk)
            val title=Paragraph("Guest Details",guestTitle)
            title.alignment=Element.ALIGN_CENTER
            title.spacingAfter=30f
            document.add(title)
            document.add(para)
            val guestColumnName = listOf(
        "No", "Family Name", "Total Member", "Note",
        "Invitation", "Phone Number", "Address")
            val guestColumnSize=guestColumnName.size

            val table=PdfPTable(guestColumnSize)
            table.widthPercentage=100f
            for(column in guestColumnName){
                val cell=PdfPCell(Paragraph(column,datafont))
                cell.borderColor=com.itextpdf.text.BaseColor.BLACK
                cell.backgroundColor=com.itextpdf.text.BaseColor.GRAY
                cell.borderWidth=1f
                table.addCell(cell)
            }
            for(guest in guestdetails){
                table.addCell(createDataCell(guest.id.toString(),datafont))
                table.addCell(createDataCell(guest.name,datafont))
                table.addCell(createDataCell(guest.totalFamilyMembers,datafont))
                table.addCell(createDataCell(guest.note,datafont))
                table.addCell(createDataCell(guest.isInvitationSent,datafont))
                table.addCell(createDataCell(guest.phoneNumber,datafont))
                table.addCell(createDataCell(guest.address,datafont))
            }
            document.add(table)
            document.close()
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("PDF","File Failed To Generate ${e.message}")
        }
    }
    private fun generatedVendorPDF(db: LocalDatabase, pdfPath: File){
        val vendorDetails=db.getAllVendors()
        val totalbudget=db.GetTotalVendorBudget()
        val totalbudgetpaid=db.GetVendorPaidAmount()
        val totalbudgetnotpaid=db.GetVendorNotPaidAmount()
        val eventDetails=db.getEventData(1)
        val eventName=eventDetails?.name
        val parentDirectory=File(Environment.getExternalStorageDirectory(),"Eventify")
        if(!parentDirectory.exists()){
            parentDirectory.mkdirs()
        }
        val eventDirectroy=File(parentDirectory,"$eventName")
        if(!eventDirectroy.exists()){
            eventDirectroy.mkdirs()
        }
        val pdfFilePath=File(eventDirectroy,"VendorReport.pdf")
        try {
            pdfFilePath.createNewFile()
            val document=Document(PageSize.A4)
            val headerFooterEvent=HeaderFooterEvent()
            val pdfWriter=PdfWriter.getInstance(document,FileOutputStream(pdfFilePath))
            pdfWriter.pageEvent=headerFooterEvent
            document.open()
            val imgFile = R.drawable.app_logo
            val bitmap = BitmapFactory.decodeResource(resources, imgFile)
            val byteArray = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
            val image = Image.getInstance(byteArray.toByteArray())
            image.scaleToFit(100f, 100f)
            image.alignment = Element.ALIGN_LEFT
            document.add(image)
            val vendorTitle= Font(Font.FontFamily.TIMES_ROMAN,20f, Font.BOLD)
            val dataFont= Font(Font.FontFamily.TIMES_ROMAN,15f)
            val parafont=Font(Font.FontFamily.TIMES_ROMAN,15f,Font.BOLD)

            val para=Paragraph()
            val paravalue="Total Budget:$totalbudget \n" +
                    "Total Amount Paid:$totalbudgetpaid \n" +
                    "Total Amount UnPaid:$totalbudgetnotpaid "
            val chunk=Chunk(paravalue,parafont)
            para.add(chunk)
            para.alignment=Element.ALIGN_RIGHT
            para.spacingAfter=30f
            val title=Paragraph("Vendor Details",vendorTitle)
            title.alignment=Element.ALIGN_CENTER
            title.spacingAfter=30f
            document.add(title)
            document.add(para)
            val vendorcolumnNames = listOf(
                "No","Name", "Category",
                "Estimated Amount", "Balance",
                "Notes", "Phone Number",
                "Email", "Website", "Address")
            val vendorColumnSize=vendorcolumnNames.size

            val table=PdfPTable(vendorColumnSize)
            table.widthPercentage=100f

            for(column in vendorcolumnNames){
                val cell=PdfPCell(Paragraph(column,dataFont))
                cell.borderColor=com.itextpdf.text.BaseColor.BLACK
                cell.backgroundColor=com.itextpdf.text.BaseColor.GRAY
                cell.borderWidth=1f
                table.addCell(cell)
            }
            for (vendor in vendorDetails){
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
    private fun sepratePDF(){
        GlobalScope.launch{
         val db=DatabaseManager.getDatabase(applicationContext)
        val eventDetails = db.getEventData(1)
        val eventName = eventDetails?.name

        val parentDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs()
        }
        val eventDirectroy=File(parentDirectory,"$eventName")
        if(!eventDirectroy.exists()){
            eventDirectroy.mkdirs()
        }
        generateGuestPDF(db, eventDirectroy)
        generatedVendorPDF(db, eventDirectroy)
        generateTaskPDF(db, eventDirectroy)
        generateBudgetPDF(db, eventDirectroy)
    }
        }
    private fun generatePDF() {
        GlobalScope.launch {
         val db=DatabaseManager.getDatabase(applicationContext)
        val eventDetails = db.getEventData(1)
        val eventName = eventDetails?.name

        val parentDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs()
        }
        val eventDirectroy=File(parentDirectory,"$eventName")
        if(!eventDirectroy.exists()){
            eventDirectroy.mkdirs()
        }
        val mergedPdfPath = File(eventDirectroy, "MergedReport.pdf").absolutePath

        generateGuestPDF(db, eventDirectroy)
        generatedVendorPDF(db, eventDirectroy)
        generateTaskPDF(db, eventDirectroy)
        generateBudgetPDF(db, eventDirectroy)

        try {
            mergePDFs(mergedPdfPath,
                File(eventDirectroy, "GuestReport.pdf").absolutePath,
                File(eventDirectroy, "VendorReport.pdf").absolutePath,
                File(eventDirectroy, "TaskReport.pdf").absolutePath,
                File(eventDirectroy, "BudgetReport.pdf").absolutePath
            )
            Toast.makeText(applicationContext, "PDF files merged successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PDF", "Failed to merge PDFs: ${e.message}")
        }
    }
        }

    private fun mergePDFs(outputFilePath: String, vararg pdfFilePaths: String) {
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
    private fun checkPermissions() = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            WRITE_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE
        ),permissioncode)
    }
    private fun getThemePreference(context:Context, key:String):Int{
        val shared=context.getSharedPreferences("Theme",Context.MODE_PRIVATE)
        return shared.getInt(key,AppCompatDelegate.MODE_NIGHT_NO)
    }
    private fun showFirstLaunchDialog() {
        val eventadding=EventAdding(this,supportFragmentManager,null)
        eventadding.show()

    }
    private fun isFirstLaunch(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean("isFirstLaunch", true)
    }
   private fun setFirstLaunchFlag(context: Context, isFirstLaunch: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean("isFirstLaunch", isFirstLaunch).apply()
    }

    override fun onResume() {
        super.onResume()
        setAppTheme()
        showEventData()
        swipeRefreshLayout.isRefreshing=true
        Handler().postDelayed({

            swipeRefreshLayout.isRefreshing = false
        }, 10)
    }
    @SuppressLint("Range")
    fun showEventData() {
        val db=DatabaseManager.getDatabase(this)
        eventList = db.getAllEvents()
        val eventtimer = db.getEventData(1)
        val budgettotdal=db.getTotalBudget()
        val budgetPaid=db.getTotalPaidBudget()
        val budgetNotPaid=db.getTotalNotPaidBudget()
        val taskComtext=db.getTaskStatus()
        val taskpending=db.getTaskPendingStatus()
        val totaltask=db.getTotalTask()
        val totalGuest=db.getTotalInvitation()
        val vendorTotalAmt=db.GetTotalVendorBudget()
        val vendorTotalPaid=db.GetVendorPaidAmount()
        val vendorTotalNotPaid=db.GetVendorNotPaidAmount()
        val totalInvitaionSnt=db.getTotalInvitationsSent()
        val totalInvitaionNotSnt=db.getTotalInvitationsNotSent()
        setSummary(totalInvi,totalGuest.toString())
        setSummary(totalInvitationSent,totalInvitaionSnt.toString())
        setSummary(totalInvitationNotSent,totalInvitaionNotSnt.toString())
        setSummary(taskCompleted,taskComtext.toString())
        setSummary(taskPending,taskpending.toString())
        setSummary(totalTask,totaltask.toString())
        setSummary(vendorTotalAmount,vendorTotalAmt.toString())
        setSummary(vendorPaidAmount,vendorTotalPaid.toString())
        setSummary(vendorPendingAmount,vendorTotalNotPaid.toString())
        setSummary(budgetShowTextView,budgettotdal.toString())
        setSummary(paidAmountShowTextView,budgetPaid.toString())
        setSummary(pendingAmountShowTextView,budgetNotPaid.toString())

        if (eventtimer !=  null) {
            eventname.text = eventtimer.name
            val eventDate = eventtimer.Date
            val eventTime = eventtimer.time
            val currentDate = Calendar.getInstance().time
            val eventDateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).parse("$eventDate $eventTime")
            if (eventDateTime != null) {
                val remainingTimeInMillis = eventDateTime.time - currentDate.time
                countDownTimer?.cancel()
                countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val days = millisUntilFinished / (24 * 60 * 60 * 1000)
                        val hours = (millisUntilFinished % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)

                        val minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000)
                        val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                        val remainingTime = String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds)
                        eventTimerDisplay.text = remainingTime
                    }
                    override fun onFinish() {
                        eventTimerDisplay.text = "Event Started"
                    }
                }.start()
            } else {
                Log.e("CountdownError", "Error parsing event date and time.")
            }
        }
        adapter = EventLayoutAdapter(eventList){ position ->
        }
        adapter.updateData(eventList)
        eventRecyclerView.adapter = adapter
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

    }
    private fun setSummary(textField:TextView,value:String?){
        if(value.isNullOrEmpty() || value=="0" || value=="0.0"){
            textField.text="No Data Found"
        }else{
            textField.text=value.toString()
        }
    }

    private fun deleteEvent(event: Events):Boolean {
        val db = DatabaseManager.getDatabase(this)
        db.deleteEvent(event)
        // Close the database if it's open
        DatabaseManager.getDatabase(this).close()

        // Delete the database file
        val databaseDeleted = this.deleteDatabase(event.name)

        if (databaseDeleted) {
            Toast.makeText(this, "Event Removed", Toast.LENGTH_SHORT).show()
            recreate() // This will recreate the activity to reflect the changes
        } else {
            Toast.makeText(this, "Failed to remove event", Toast.LENGTH_SHORT).show()
        }

        return databaseDeleted
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

        // Checking if the widget is already added
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            val successCallback = PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, EventWidget::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            appWidgetManager.requestPinAppWidget(myWidgetProvider, null, successCallback)
        }
    }


    //Navigation Drawer function
    private fun navigationDrawershow() {

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Open the drawer when the navigation button is clicked
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_whatsapp->{
                    sendWhatsAppMessage()
                    true
                }
                R.id.nav_About -> {
                    try{
                        Intent(this, About::class.java).also { startActivity(it) }
                    }catch (e:Exception){
                        Log.d("Activity","Activity ${e.message} ")
                    }
                    true
                }

                R.id.nav_settings->{
                    try{
                        Intent(this, SettingActivity::class.java).also { startActivity(it) }
                    }catch (e:Exception){
                        Log.d("Activity","Activity ${e.message} ")
                    }
                    true
                }
                R.id.nav_logout->{
                    userLogout()
                    true
                }
                R.id.nav_manage_event->{
                    Intent(this, EventList::class.java).also { startActivity(it) }
                    true
                }
                R.id.widget->{
                    addWidgetToHomeScreen()
                    true
                }
                R.id.nav_share->{
                    val applink="\"Hey There ! \uD83D\uDC4B I've been this fantastic Event Manager app,and it's Save My Time for Manageing Event Time and Effort. \uD83D\uDCB0 if you're looking for a simple and effective way to manage your event,I Highly recommend giving it a try .You can download it here : https://bit.ly/3s4z8py "
                    val intent=Intent()
                    intent.action=Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT,applink)
                    intent.type="text/plain"
                    startActivity(Intent.createChooser(intent,"Share to:"))
                    drawerLayout.close()
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

    private fun userLogout() {
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut().addOnCompleteListener(this) { task ->
            val intentClass = if (task.isSuccessful) Login_SignUp_Option::class.java else signin_account::class.java
            startActivity(Intent(this, intentClass))
            finish()
            val message = if (task.isSuccessful) "Logout Successful" else "Something went wrong"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        FirebaseAuth.getInstance().signOut()
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
//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(dataAddedReceiver)
//
//    }
}
