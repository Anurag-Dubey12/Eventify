package com.example.eventmatics

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventLayoutAdapter
import com.example.eventmatics.BroadCastReceiver.EventNotificationReceiver
import com.example.eventmatics.Events_Data_Holder_Activity.BudgetDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.GuestDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.TaskDataHolderActivity
import com.example.eventmatics.Events_Data_Holder_Activity.VendorDataHolderActivity
import com.example.eventmatics.Login_Activity.signin_account
import com.example.eventmatics.NavigationDrawer.PDF_Report
import com.example.eventmatics.NavigationDrawer.ProfileActivity
import com.example.eventmatics.NavigationDrawer.SettingActivity
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfile
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfileDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.example.eventmatics.fragments.DatabaseNameHolder
import com.example.eventmatics.fragments.EventAdding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfCopy
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfWriter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(),DatabaseNameHolder.DatabaseChangeListener{
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toogle:ActionBarDrawerToggle
    private var countDownTimer:CountDownTimer?=null
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var taskImageButton: ImageButton
    private lateinit var budgetImageButton: ImageButton
    private lateinit var Imageadd: CircleImageView
    private lateinit var guestImageButton: ImageButton
    private lateinit var vendorImageButton: ImageButton
    private lateinit var budgetInfoCardView: CardView
    private lateinit var budgetShowTextView: TextView
    private lateinit var VendorTotalAmount: TextView
    private lateinit var TaskCompleted: TextView
    private lateinit var VendorPendingAmount: TextView
    private lateinit var VendorPaidAmount: TextView
    private lateinit var TaskPending: TextView
    private lateinit var Nameadd: TextView
    private lateinit var TotalTask: TextView
    private lateinit var TotalInvi: TextView
    private lateinit var TotalInvitationSent: TextView
    private lateinit var TotalInvitationNotSent: TextView
    private lateinit var Eventshow: MaterialButton
    private lateinit var BudgetSummary: TextView
    private lateinit var GuestSummary: TextView
    private lateinit var TaskSummary: TextView
    private lateinit var VendorSummary: TextView
    private lateinit var eventname: TextView
    private lateinit var EventTimerDisplay: TextView
//    private lateinit var crossimg: ImageView
    private lateinit var eventshowhide: LinearLayout
    private lateinit var eventActivity: LinearLayout
    private lateinit var pendingAmountShowTextView: TextView
    private lateinit var paidAmountShowTextView: TextView
    private lateinit var eventaddbut: MaterialButton
    private lateinit var adapter: EventLayoutAdapter
    private  var eventList:MutableList<Events> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dataAddedReceiver: BroadcastReceiver
    private lateinit var EditProfile: MaterialButton
    private lateinit var Delete_Event: MaterialButton
    private lateinit var GeneratePdf: MaterialButton
    private lateinit var SaveButton: MaterialButton
    private lateinit var ProfileDialog: BottomSheetDialog
    private lateinit var ImageAddOption: BottomSheetDialog
//    private lateinit var piechart:PieChart
    private val PERMISSION_CODE=101
    private val PICK_FILE_REQUEST = 1
    private val CAPTURE_REQ_CODE = 101
    private val GALLERY_REQ_CODE = 201
    private var selectedImageUri:Uri?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawerlayout)
        navView= findViewById(R.id.navView)
        eventRecyclerView = findViewById(R.id.Eventrec)
        taskImageButton = findViewById(R.id.task)
        TaskPending = findViewById(R.id.TaskPending)
        VendorPendingAmount = findViewById(R.id.VendorPendingAmount)
        eventname = findViewById(R.id.eventname)
        VendorPaidAmount = findViewById(R.id.VendorPaidAmount)
        TotalInvi = findViewById(R.id.TotalInvi)
        BudgetSummary = findViewById(R.id.BudgetSummary)
        TaskSummary = findViewById(R.id.TaskSummaryText)
        VendorSummary = findViewById(R.id.VendorSummary)
        GuestSummary = findViewById(R.id.GuestSummaryText)
        TotalInvitationSent = findViewById(R.id.TotalInviSent)
        TotalInvitationNotSent = findViewById(R.id.TotalInvinotSent)
        eventActivity = findViewById(R.id.eventActivity)
        Delete_Event = findViewById(R.id.Delete_Event)
        VendorTotalAmount = findViewById(R.id.VendorTotalAmount)
        TaskCompleted = findViewById(R.id.TaskCompleted)
        GeneratePdf = findViewById(R.id.GeneratePdf)
        TotalTask = findViewById(R.id.TotalTask)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
        EventTimerDisplay = findViewById(R.id.EventTimerDisplay)
//        crossimg = findViewById(R.id.crossimg)
        eventaddbut = findViewById(R.id.eventaddbut)
        budgetImageButton = findViewById(R.id.budget)
        eventshowhide = findViewById(R.id.eventshowhide)
        guestImageButton = findViewById(R.id.Guest)
        vendorImageButton = findViewById(R.id.Vendor)
        Eventshow = findViewById(R.id.eventnameshow)

//        piechart = findViewById(R.id.piechart)
//        taskRecyclerView = findViewById(R.id.TaskRec)
        budgetInfoCardView = findViewById(R.id.budget_info)
        budgetShowTextView = findViewById(R.id.Budgetshow)
        pendingAmountShowTextView = findViewById(R.id.PendingAmountshow)
        paidAmountShowTextView = findViewById(R.id.PaidAmountshow)
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
//        widgetButton = findViewById(R.id.widgetbutton)
        //For First Time Launch Event Create SHow
        if (isFirstLaunch(this)) {
            showFirstLaunchDialog()
            setFirstLaunchFlag(this, false)
        }
        Editprofile()
        val databaseNameHolder = DatabaseNameHolder(this)
        databaseNameHolder.setDatabaseChangeListener(this)

        //Load Profile Pic
        val db=UserProfileDatabase(this)
        val headerView = navView.getHeaderView(0)
        val userprofile=headerView.findViewById<CircleImageView>(R.id.profilepic)
        val username=headerView.findViewById<TextView>(R.id.UserNameView)
        EditProfile=headerView.findViewById(R.id.editProfileButton)
        EditProfile.setOnClickListener {
            Editprofile()
        }
        val Userimage=db.getUserProfilebyID(1)
        val userimage=Userimage?.Image
        if(Userimage!=null){
            val image=BitmapFactory.decodeByteArray(userimage,0, userimage!!.size)
            userprofile.setImageBitmap(image)
        }
        val UserName=Userimage?.name
        if(UserName!=null){
            username.text= UserName.toString()
        }
        createNotificationChannel()
        if (eventRecyclerView.adapter?.itemCount == 0) {
            EventTimerDisplay.text=" "
            eventname.text=" "
            val eventAdding = EventAdding(this, supportFragmentManager, null)
            eventAdding.show() }
        else {
            GuestSummary.setOnClickListener { CheckAndStartActivity(GuestDataHolderActivity::class.java) }
            TaskSummary.setOnClickListener { CheckAndStartActivity(TaskDataHolderActivity::class.java) }
            BudgetSummary.setOnClickListener { CheckAndStartActivity(BudgetDataHolderActivity::class.java) }
            VendorSummary.setOnClickListener { CheckAndStartActivity(VendorDataHolderActivity::class.java) }
            taskImageButton.setOnClickListener { CheckAndStartActivity(TaskDataHolderActivity::class.java) }
            budgetImageButton.setOnClickListener { CheckAndStartActivity(BudgetDataHolderActivity::class.java) }
            guestImageButton.setOnClickListener { CheckAndStartActivity(GuestDataHolderActivity::class.java) }
            vendorImageButton.setOnClickListener { CheckAndStartActivity(VendorDataHolderActivity::class.java) } }
        Eventshow.setOnClickListener {
            val isRecyclerViewVisible = eventRecyclerView.visibility == View.VISIBLE
            val isActivityVisible = eventActivity.visibility == View.VISIBLE

            Eventshow.icon = if (isRecyclerViewVisible) getDrawable(R.drawable.show_event) else getDrawable(R.drawable.up_arrow)
            eventRecyclerView.visibility = if (isRecyclerViewVisible) View.GONE else View.VISIBLE
            eventaddbut.visibility = if (isRecyclerViewVisible) View.GONE else View.VISIBLE
            eventActivity.visibility = if (isActivityVisible) View.GONE else View.VISIBLE
        }

        eventaddbut.setOnClickListener {
            val eventadding=EventAdding(this,supportFragmentManager,null)
            eventadding.show() }
        GeneratePdf.setOnClickListener {
                MaterialAlertDialogBuilder(this)
                    .setTitle("PDF")
                    .setMessage("Select in Which format you want pdf")
                    .setNeutralButton("Seprate PDF"){dialog,_->
                        if(checkPermissions()){
                            SepratePDF()
                            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            requestPermission()
                        }

                    }
                    .setPositiveButton("Combined PDF"){dialog,_->
                        if(checkPermissions()){
                            GeneratePDF()
                            Toast.makeText(this, "PDF Generated Successfully", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            requestPermission()
                        }
                    }
                    .show()

        }
        Delete_Event.setOnClickListener {
            if(eventRecyclerView.adapter?.itemCount==0){
               Delete_Event.isCheckable=false
            }
            else{
            val position=0
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
        }}
//        widgetButton.setOnClickListener { addWidgetToHomeScreen() }
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

    private fun Editprofile() {
        ProfileDialog= BottomSheetDialog(this)
        ProfileDialog.setContentView(R.layout.editprofiledialog)
        ProfileDialog.show()

        val db=UserProfileDatabase(this)
        Imageadd=ProfileDialog.findViewById(R.id.uploadImage)!!
        Nameadd=ProfileDialog.findViewById(R.id.uploadName)!!
        SaveButton=ProfileDialog.findViewById(R.id.saveButton)!!

        val UserData=db.getUserProfilebyID(1)
        val currentname=UserData?.name ?:" "
        val userimage=UserData?.Image
        try{


        if(UserData!=null){
            val image=BitmapFactory.decodeByteArray(userimage,0, userimage!!.size)
            Imageadd.setImageBitmap(image)
        }

        Imageadd.setOnClickListener {
            ImageUpload()
        }
        Nameadd.text=currentname
        }catch (e:Exception){
            Log.d("Image","Crash due to:${e.message}")
        }
        SaveButton.setOnClickListener {
//            ImageUpload()
//            ImageAddOption.dismiss()
//            ProfileDialog.dismiss()

            if(selectedImageUri!=null){
                val GetImageByte=getImageByte(selectedImageUri!!)
                val userid=FirebaseAuth.getInstance().currentUser?.uid
                if(userid!=null){
                    var username = Nameadd.text.toString()
                    val existinguser=db.getUserProfilebyID(1)
                    if(existinguser!=null){
                        val userprofile=UserProfile(1,username,GetImageByte)
                        db.updateUserProfile(userprofile)
                        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
                        val image=BitmapFactory.decodeByteArray(GetImageByte,0,GetImageByte.size)
                        Imageadd.setImageBitmap(image)
//                        recreate()
                    }
                    else{
                        val userprofile=UserProfile(1,username,GetImageByte)
                        db.insertUserProfile(userprofile)
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                        val image=BitmapFactory.decodeByteArray(GetImageByte,0,GetImageByte.size)
                        Imageadd.setImageBitmap(image)
//                        recreate()
                    }
                }
            }else{
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
            ProfileDialog.dismiss()
            drawerLayout.close()
        }
    }
    private fun ImageUpload(){
        ImageAddOption=BottomSheetDialog(this)
        ImageAddOption.setContentView(R.layout.profiledialog)
        ImageAddOption.show()

        val ImageCapture=ImageAddOption.findViewById<ImageView>(R.id.ImageCapture)
        val ImageGallery=ImageAddOption.findViewById<ImageView>(R.id.ImageGallery)

        ImageGallery?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                    arrayOf( android.Manifest.permission.CAMERA),GALLERY_REQ_CODE)
            }else{
                Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also {
                    startActivityForResult(it,GALLERY_REQ_CODE)
                    ImageAddOption.dismiss()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val db=UserProfileDatabase(this)
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                GALLERY_REQ_CODE->{
                    val selecteddata=data?.data
                    if(selecteddata!=null){
                        selectedImageUri=selecteddata
                        val getimagebyte=getImageByte(selecteddata)
                        val image=BitmapFactory.decodeByteArray(getimagebyte,0,getimagebyte.size)
                        Imageadd.setImageBitmap(image)
                    }
                }
            }
        }
    }

    fun getImageByte(image: Uri):ByteArray{
        val image=contentResolver.openInputStream(image)
        return image?.readBytes() ?: ByteArray(0)
    }

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channelId="Event_Notification"
            val channelName="Event Notification"
            val importance=NotificationManager.IMPORTANCE_HIGH
            val channel=NotificationChannel(channelId,channelName,importance)
            val notificationManager=getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

    }
    @SuppressLint("ScheduleExactAlarm")
    fun ScheduleEventNotification(events: List<Events>) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (event in events) {
            val eventDateTime = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).parse("${event.Date} ${event.time}")
            val calendar = Calendar.getInstance()
            calendar.time = eventDateTime
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val notificationTime = calendar.timeInMillis

            val intent = Intent(this, EventNotificationReceiver::class.java)
            intent.putExtra("event_name", event.name)

            val requestCode = event.id.toInt()
            val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent)
        }
    }

    override fun onDatabaseChanged(EventID: Long?) {
        val databasename=getSharedPreference(this,"databasename").toString()
        val db = LocalDatabase(this, databasename)
        val SwithEvent=db.getSwitchEventData(EventID)
        val Eventtimer=db.getEventData(EventID)
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
        adapter = EventLayoutAdapter(SwithEvent) { position ->
            val popup = PopupMenu(eventshowhide.context, eventshowhide)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Event_Pdf -> {
                        val options = arrayOf("Combined", "Separate")

                        MaterialAlertDialogBuilder(eventshowhide.context)
                            .setTitle("PDF Format")
                            .setSingleChoiceItems(options, -1) { dialog, which ->
                                when (which) {
                                    0 -> {
                                        // Combined option selected
                                        if (checkPermissions()) {
                                            GeneratePDF()
                                        } else {
                                            requestPermission()
                                        }
                                    }
                                    1 -> {
                                        // Separate option selected
                                        if (checkPermissions()) {
                                            SepratePDF()
                                        } else {
                                            requestPermission()
                                        }
                                    }
                                }
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()

                        true
                    }
                    R.id.event_delete -> {
                        // Handle delete option
                        // You can add your delete logic here
                        true
                    }
                    else -> return@setOnMenuItemClickListener false
                }
            }
            popup.show()
        }

        eventRecyclerView.adapter = adapter
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing=false
    }

    private fun SetAppTheme() {
        val Theme=GetThemePreference(this,"Theme")
        AppCompatDelegate.setDefaultNightMode(Theme)
    }
    fun CheckAndStartActivity(targetActivity:Class<*>){
        if (eventRecyclerView.adapter?.itemCount==0){
            val eventAdding=EventAdding(this,supportFragmentManager,null)
            eventAdding.show()
            Toast.makeText(this, "Create an Event Before Moving Further", Toast.LENGTH_SHORT).show()
        }
        else{
            Intent(this,targetActivity).also { startActivity(it) }
        }
    }

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
    @SuppressLint("SuspiciousIndentation")
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
    fun SepratePDF(){
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
        GenerateGuestPDF(db, EventDirectroy)
        GeneratedVendorPDF(db, EventDirectroy)
        GeneratedTaskPDF(db, EventDirectroy)
        GenerateBudgetPDF(db, EventDirectroy)
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


    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
    fun GetThemePreference(context:Context,key:String):Int{
        val shared=context.getSharedPreferences("Theme",Context.MODE_PRIVATE)
        return shared.getInt(key,AppCompatDelegate.MODE_NIGHT_NO)
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
        SetAppTheme()
        showEventData()
    }
    @SuppressLint("Range")
     fun showEventData() {
        val databasename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databasename)
        eventList = db.getAllEvents()
        val Eventtimer = db.getEventData(1)
        val budgettotdal=db.getTotalBudget()
        val BudgetPaid=db.getTotalPaidBudget()
        val BudgetNotPaid=db.getTotalNotPaidBudget()
        val budgetUnPaid=db.getTotalUnPaid()
        val TaskComtext=db.getTaskStatus()
        val Taskpending=db.getTaskPendingStatus()
        val totaltask=db.getTotalTask()
        val TotalGuest=db.getTotalInvitation()
        val VendorTotalAmt=db.GetTotalVendorBudget()
        val VendorTotalPaid=db.GetVendorPaidAmount()
        val VendorTotalNotPaid=db.GetVendorNotPaidAmount()
        val TotalInvitaionSnt=db.getTotalInvitationsSent()
        val TotalInvitaionNotSnt=db.getTotalInvitationsNotSent()
        SetSummary(TotalInvi,TotalGuest.toString())
        SetSummary(TotalInvitationSent,TotalInvitaionSnt.toString())
        SetSummary(TotalInvitationNotSent,TotalInvitaionNotSnt.toString())
        SetSummary(TaskCompleted,TaskComtext.toString())
        SetSummary(TaskPending,Taskpending.toString())
        SetSummary(TotalTask,totaltask.toString())
        SetSummary(VendorTotalAmount,VendorTotalAmt.toString())
        SetSummary(VendorPaidAmount,VendorTotalPaid.toString())
        SetSummary(VendorPendingAmount,VendorTotalNotPaid.toString())
        SetSummary(budgetShowTextView,budgettotdal.toString())
        SetSummary(paidAmountShowTextView,BudgetPaid.toString())
        SetSummary(pendingAmountShowTextView,BudgetNotPaid.toString())

        if (Eventtimer !=  null) {
            Eventshow.text = Eventtimer.name
            budgetShowTextView.text = Eventtimer.budget
            val budget=Eventtimer.budget
//            piechart.clearChart()
//            piechart.addPieSlice(PieModel("Event",budget.toFloat(),Color.parseColor("#2ecc71")))
//            piechart.addPieSlice(PieModel("Paid",budgettotdal.toFloat(),Color.parseColor("#3498db")))
//            piechart.addPieSlice(PieModel("UnPaid",budgetUnPaid.toFloat(),Color.parseColor("#e74c3c")))
//            piechart.startAnimation()
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
        }
        ScheduleEventNotification(eventList)
        adapter.updateData(eventList)
        eventRecyclerView.adapter = adapter
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
    fun SetSummary(TextField:TextView,value:String?){
        if(value.isNullOrEmpty() || value=="0" || value=="0.0"){
            TextField.text="No Data Found"
        }else{
            TextField.text=value.toString()
        }
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
                R.id.home -> {
                    // Open the drawer when the navigation button is clicked
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_whatsapp->{
                    sendWhatsAppMessage()
                    true
                }
                R.id.nav_telegram -> {
                    val userid = 1302795295
                    val message = "Hello, this is your message!"
                    val telegramUri = Uri.parse("tg://send?user_id=$userid&text=${Uri.encode(message)}")
                    val telegramIntent = Intent(Intent.ACTION_VIEW, telegramUri)
                    telegramIntent.setPackage("org.telegram.messenger")

                    val packageManager = packageManager
                    val activities = packageManager.queryIntentActivities(telegramIntent, 0)
                    if (activities.isNotEmpty()) {
                        startActivity(telegramIntent)
                    } else {
                        Toast.makeText(this, "Telegram app is not installed", Toast.LENGTH_SHORT).show()
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
                R.id.nav_pdf->{
                    Intent(this,PDF_Report::class.java).also { startActivity(it) }

                    true
                }
                R.id.nav_profile->{
                    try {
                    Intent(this,ProfileActivity::class.java).also { startActivity(it) }

                    }catch (e:Exception){
                        Log.d("Activity","Activity ${e.message} ")
                    }
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
