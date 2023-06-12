package com.example.eventmatics

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
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
import com.example.eventmatics.Adapter.EventLayoutAdapter
import com.example.eventmatics.Event_Data_Holder.BudgetDataHolderActivity
import com.example.eventmatics.Event_Data_Holder.GuestDataHolderActivity
import com.example.eventmatics.Event_Data_Holder.TaskDataHolderActivity
import com.example.eventmatics.Event_Data_Holder.VendorDataHolderActivity
import com.example.eventmatics.Login_Activity.signin_account
import com.example.eventmatics.NavigationDrawer.ProfileActivity
import com.example.eventmatics.NavigationDrawer.SettingActivity
import com.example.eventmatics.data_class.Eventlayourdata
import com.example.eventmatics.fragments.EventAdding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(),EventAdding.EventAddingListener {
        lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toogle:ActionBarDrawerToggle
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var taskImageButton: ImageButton
    private lateinit var budgetImageButton: ImageButton
    private lateinit var guestImageButton: ImageButton
    private lateinit var vendorImageButton: ImageButton
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var budgetInfoCardView: CardView
    private lateinit var budgetShowTextView: TextView
    private lateinit var Eventshow: TextView
    private lateinit var crossimg: ImageView
    private lateinit var eventshowhide: LinearLayout
    private lateinit var pendingAmountShowTextView: TextView
    private lateinit var paidAmountShowTextView: TextView
    private lateinit var eventaddbut: AppCompatButton
    private lateinit var widgetButton: Button

    lateinit var adapter:EventLayoutAdapter
    lateinit var eventdata:MutableList<Eventlayourdata>
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
        crossimg = findViewById(R.id.crossimg)
        eventaddbut = findViewById(R.id.eventaddbut)
        budgetImageButton = findViewById(R.id.budget)
        eventshowhide = findViewById(R.id.eventshowhide)
        guestImageButton = findViewById(R.id.Guest)
        vendorImageButton = findViewById(R.id.Vendor)
        Eventshow = findViewById(R.id.eventnameshow)
        taskRecyclerView = findViewById(R.id.TaskRec)
        budgetInfoCardView = findViewById(R.id.budget_info)
        budgetShowTextView = findViewById(R.id.Budgetshow)
        pendingAmountShowTextView = findViewById(R.id.PendingAmountshow)
        paidAmountShowTextView = findViewById(R.id.PaidAmountshow)
         widgetButton = findViewById(R.id.widgetbutton)


        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //Event data Recycler view
        eventdata= mutableListOf()
        adapter=EventLayoutAdapter(eventdata)
        eventRecyclerView.adapter=adapter
        eventRecyclerView.layoutManager=LinearLayoutManager(this)

        taskImageButton.setOnClickListener {
            Intent(this,TaskDataHolderActivity::class.java).also {
                startActivity(it)
            }
        }
        budgetImageButton.setOnClickListener {
            Intent(this, BudgetDataHolderActivity::class.java).also {
                startActivity(it)
            }
        }
        guestImageButton.setOnClickListener {
            Intent(this,GuestDataHolderActivity::class.java).also {
                startActivity(it)
            }
        }
        vendorImageButton.setOnClickListener {
            Intent(this,VendorDataHolderActivity::class.java).also {
                startActivity(it)
            }
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
            }
            else{
                eventshowhide.visibility=View.VISIBLE
                eventRecyclerView.visibility=View.VISIBLE
                eventaddbut.visibility=View.VISIBLE
                Eventshow.visibility=View.GONE
            }
        }
        eventaddbut.setOnClickListener {
            val eventadding=EventAdding(this,supportFragmentManager)
            eventadding.setEventAddingListener(this)
            eventadding.show()
        }
        widgetButton.setOnClickListener {
            // Call a function to add the widget
            addWidgetToHomeScreen()
        }
        navigationDrawershow()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
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

    override fun onEventCreated(
        eventName: String,
        eventDate: String,
        eventTime: String,
        budget: String
    ) {
        val data=Eventlayourdata(eventName,eventDate,eventTime)
        eventdata.add(data)
        Eventshow.text=eventName
        adapter.notifyDataSetChanged()
        budgetShowTextView.text=budget+"$"
    }

}
