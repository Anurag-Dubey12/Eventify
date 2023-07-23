package com.example.eventmatics

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
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
import com.example.eventmatics.NavigationDrawer.ProfileActivity
import com.example.eventmatics.NavigationDrawer.SettingActivity
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.example.eventmatics.fragments.AllDatabase
import com.example.eventmatics.fragments.EventAdding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
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
    private lateinit var widgetButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dataAddedReceiver: BroadcastReceiver
    private lateinit var piechart:PieChart
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
        widgetButton = findViewById(R.id.widgetbutton)

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        taskImageButton.setOnClickListener { Intent(this,TaskDataHolderActivity::class.java).also { startActivity(it) } }
        budgetImageButton.setOnClickListener { Intent(this, BudgetDataHolderActivity::class.java).also { startActivity(it) } }
        guestImageButton.setOnClickListener { Intent(this,GuestDataHolderActivity::class.java).also { startActivity(it) } }
        vendorImageButton.setOnClickListener { Intent(this,VendorDataHolderActivity::class.java).also { startActivity(it) } }
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
            eventadding.show() }
        widgetButton.setOnClickListener { addWidgetToHomeScreen() }
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databasename=getSharedPreference(this,"databasename").toString()
                val databasehelper = LocalDatabase(this, databasename)
                val eventList = databasehelper.getAllEvents()
                val Eventtimer=databasehelper.getEventData(1)
                if(Eventtimer!=null){
                    Eventshow.text=Eventtimer.name
                    budgetShowTextView.text=Eventtimer.budget

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
                    Toast.makeText(this,"Event Not Found",Toast.LENGTH_SHORT).show()
                }
                val adapter = EventLayoutAdapter(eventList){ position ->
                    val eventadding=EventAdding(this,supportFragmentManager,position)
                    eventadding.show()}
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
        navigationDrawershow()
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
    // Add this function in your MainActivity class


    @SuppressLint("Range")
     fun showEventData() {
        val databasename = getSharedPreference(this, "databasename").toString()
        val databasehelper = LocalDatabase(this, databasename)
        val eventList = databasehelper.getAllEvents()
        val Eventtimer = databasehelper.getEventData(1)

        if (Eventtimer != null) {
            Eventshow.text = Eventtimer.name
            budgetShowTextView.text = Eventtimer.budget
            val budget=Eventtimer.budget
            piechart.addPieSlice(PieModel("Budget",budget.toFloat(),Color.parseColor("#959494")))
            piechart.addPieSlice(PieModel("Budget",budget.toFloat(),Color.parseColor("#FF63A1")))
            piechart.addPieSlice(PieModel("Budget",budget.toFloat(),Color.parseColor("#F6D661")))
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
            Toast.makeText(this, "Event Not Found", Toast.LENGTH_SHORT).show()
        }

        val adapter = EventLayoutAdapter(eventList){ position ->
            val eventadding=EventAdding(this,supportFragmentManager,position)
            eventadding.show()}
        eventRecyclerView.adapter = adapter
        eventRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter.notifyDataSetChanged()
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
        // Set the item click listener for the NavigationView
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
                R.id.nav_profile->{
                    Intent(this,ProfileActivity::class.java).also { startActivity(it) }

                    true
                }
                R.id.nav_logout->{
                   userlogout()
                    true
                }
                R.id.nav_manage_event->{
                    val EventName=AllDatabase(this)
                    EventName.show()
                    true
                }
                else -> false
            }
        }
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
