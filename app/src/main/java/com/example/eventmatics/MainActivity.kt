package com.example.eventmatics

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.eventmatics.Event_Activities.Budget
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.Event_Details_Activity.GuestDetails
import com.example.eventmatics.Event_Details_Activity.TaskDetails
import com.example.eventmatics.Event_Details_Activity.VendorDetails
import com.example.eventmatics.fragments.BudgetFragment
import com.example.eventmatics.fragments.GuestFragment
import com.example.eventmatics.fragments.VendorFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
        lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    lateinit var toogle:ActionBarDrawerToggle
    lateinit var bottomnav: BottomNavigationView


    //Animation to make texview and Button to be Appear
    private val frombottomfabanim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }

    private val tobottomfabanim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }

    private val rotateclock: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise)
    }

    private val anticlockwise: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anticlock_wise)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView= findViewById(R.id.navView)
        drawerLayout=findViewById(R.id.drawerlayout)


        bottomnav = findViewById(R.id.bottomnav)
        bottomnav.background = null
//        bottomnav.menu.getItem(2).isEnabled = false
        navigationDrawershow()

        bottomNavigationclickListener()


    }


    private fun bottomNavigationclickListener() {
        val bugetfrag= budgetdataholderfragment()
        val vendorfrag=VendorFragment()
        val guestfrag=GuestFragment()
        bottomnav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.budget -> {
                    currentfragment(bugetfrag)

                    true
                }
                R.id.vendor -> {
                    currentfragment(vendorfrag)

                    true
                }
                R.id.guest -> {

                    currentfragment(guestfrag)

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
}