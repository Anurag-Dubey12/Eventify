package com.example.eventmatics

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Event_Data_Holder.BudgetDataHolderActivity
import com.example.eventmatics.Event_Data_Holder.GuestDataHolderActivity
import com.example.eventmatics.Event_Data_Holder.TaskDataHolderActivity
import com.example.eventmatics.Event_Data_Holder.VendorDataHolderActivity
import com.example.eventmatics.Login_Activity.signin_account
import com.example.eventmatics.Navigation.Settings
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
        lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toogle:ActionBarDrawerToggle
    lateinit var bottomnav: BottomNavigationView
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var taskImageButton: ImageButton
    private lateinit var budgetImageButton: ImageButton
    private lateinit var guestImageButton: ImageButton
    private lateinit var vendorImageButton: ImageButton
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var budgetInfoCardView: CardView
    private lateinit var budgetShowTextView: TextView
    private lateinit var pendingAmountShowTextView: TextView
    private lateinit var paidAmountShowTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerlayout)
//        menuListView = findViewById(R.id.menu_list)
        navView= findViewById(R.id.navView)
        eventRecyclerView = findViewById(R.id.Eventrec)
        taskImageButton = findViewById(R.id.task)
        budgetImageButton = findViewById(R.id.budget)
        guestImageButton = findViewById(R.id.Guest)
        vendorImageButton = findViewById(R.id.Vendor)
        taskRecyclerView = findViewById(R.id.TaskRec)
        budgetInfoCardView = findViewById(R.id.budget_info)
        budgetShowTextView = findViewById(R.id.Budgetshow)
        pendingAmountShowTextView = findViewById(R.id.PendingAmountshow)
        paidAmountShowTextView = findViewById(R.id.PaidAmountshow)

//        menuAdapter = object : ArrayAdapter<String>(this, R.layout.drawer_layout, menuItems) {
//            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//                val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.drawer_layout, parent, false)
//
//                val menuItemTextView = view.findViewById<TextView>(R.id.menu_title)
//                val menuItemImageView = view.findViewById<ImageView>(R.id.menu_icon)
//
//                menuItemTextView.text = menuItems[position]
//                menuItemImageView.setImageResource(menuIcons[position])
//                return view
//            }
//        }

//        menuListView.adapter = menuAdapter
//        val toggle = ActionBarDrawerToggle(
//            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

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

        navigationDrawershow()

    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    //Navigation Drawer function
    private fun navigationDrawershow() {
        // Set up the ActionBarDrawerToggle
        toogle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        // Enable the "up" button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the item click listener for the NavigationView
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    Toast.makeText(applicationContext, "You clicked item 1", Toast.LENGTH_SHORT).show()
//                    drawerLayout.closeDrawers() // Close the navigation drawer
                    true
                }
                R.id.nav_whatsapp->{
                    sendwhatsappmessage()
                    true
                }
                R.id.nav_telegram->{
                    val telegramUsername = "Anurag Dubey"

                    val telegramUri = Uri.parse("https://t.me/$telegramUsername")
                    val telegramIntent = Intent(Intent.ACTION_VIEW, telegramUri)
                    startActivity(telegramIntent)
                    true
                }
                R.id.nav_settings->{
                    Intent(this,Settings::class.java).also { startActivity(it) }
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

    private fun sendwhatsappmessage() {
        // The phone number of the recipient
        val phoneNumber = "9004040592"

        // Create the WhatsApp Intent
        Intent(Intent.ACTION_SENDTO).also { intent ->
           // Set the data URI with the WhatsApp API URL and the phone number as a query parameter
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
            // Set the message content
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, I Need A Help")
            // Set the package name of WhatsApp
            intent.setPackage("com.whatsapp")

            // If there is an app installed that can handle the intent, start the activity
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // If WhatsApp is not installed on the device, show a toast message
                Toast.makeText(this, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
