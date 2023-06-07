package com.example.eventmatics

import android.annotation.SuppressLint
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
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView= findViewById(R.id.navView)
        drawerLayout=findViewById(R.id.drawerlayout)
        navigationDrawershow()

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
        }
