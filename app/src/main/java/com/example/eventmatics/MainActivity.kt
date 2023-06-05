package com.example.eventmatics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.eventmatics.data_class.BudgetDataHolderData
import com.example.eventmatics.Event_Data_Holder.Budgetdataholderfragment
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.Event_Details_Activity.GuestDetails
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.Event_Details_Activity.VendorDetails
import com.example.eventmatics.Event_Data_Holder.Guestdataholder
import com.example.eventmatics.Event_Data_Holder.Taskdataholder
import com.example.eventmatics.Event_Data_Holder.Vendordataholder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
        lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toogle:ActionBarDrawerToggle
    lateinit var bottomnav: BottomNavigationView
    lateinit var Newfab:FloatingActionButton


    //Animation to make texview and Button to be Appear
//    private val frombottomfabanim: Animation by lazy {
//        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
//    }
//
//    private val tobottomfabanim: Animation by lazy {
//        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
//    }
//
//    private val rotateclock: Animation by lazy {
//        AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise)
//    }
//
//    private val anticlockwise: Animation by lazy {
//        AnimationUtils.loadAnimation(this, R.anim.rotate_anticlock_wise)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView= findViewById(R.id.navView)
        drawerLayout=findViewById(R.id.drawerlayout)
        Newfab=findViewById(R.id.fab)


        bottomnav = findViewById(R.id.bottomnav)
        bottomnav.background = null
//        bottomnav.menu.getItem(2).isEnabled = false
        navigationDrawershow()

        bottomNavigationclickListener()
    }


    private fun bottomNavigationclickListener() {
        val bugetfrag= Budgetdataholderfragment()
        val vendorfrag=Vendordataholder()
        val Taskfrag=Taskdataholder()
        val Guestfrag=Guestdataholder()

        bottomnav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.Task -> {
                    currentfragment(Taskfrag)
                    Newfab.setOnClickListener {
                        Intent(this,TaskDetails::class.java).also { startActivity(it) }
                    }
                    true
                }
                R.id.budget -> {
                    currentfragment(bugetfrag)
                    Newfab.setOnClickListener {
                        Intent(this,BudgetDetails::class.java).also {
//                            startActivityForResult(it,100)
                        startActivity(it)}
                    }
                    true
                }
                R.id.vendor -> {
                    currentfragment(vendorfrag)
                    Newfab.setOnClickListener {
                        Intent(this,VendorDetails::class.java).also { startActivity(it) }
                    }
                    true
                }
                R.id.guest -> {

                    currentfragment(Guestfrag)
                    Newfab.setOnClickListener {
                        Intent(this,GuestDetails::class.java).also { startActivity(it) }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun currentfragment(fragment: Fragment) =supportFragmentManager.beginTransaction().apply{
        replace(R.id.fragmentcon,fragment)
        commit()
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
                else -> false
            }
        }

}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            val pending = data?.getStringExtra("pending")
            val transInfo = data?.getStringExtra("transInfo")
            val totalamt = data?.getStringExtra("totalamt")
            val paidamt = data?.getStringExtra("paidamt")

                }
            }
        }
