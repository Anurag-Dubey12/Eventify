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
    private var isExpanded = false
    private lateinit var fabConstraint:CoordinatorLayout
    private lateinit var budgettv: TextView
    lateinit var bottomnav: BottomNavigationView
    private lateinit var guesttv: TextView
    private lateinit var vendortv: TextView
    private lateinit var tasktv: TextView
    private lateinit var addbutton:FloatingActionButton
    lateinit var budgetfab: FloatingActionButton
    lateinit var guestfab: FloatingActionButton
    lateinit var vendorfab: FloatingActionButton
    lateinit var taskfab: FloatingActionButton
    lateinit var  fragmentcon:FrameLayout


     //Animation to make texview and Button to be Appear
     private val frombottomfabanim:Animation by lazy{
         AnimationUtils.loadAnimation(this,R.anim.from_bottom_fab)
     }

    private val tobottomfabanim:Animation by lazy{
        AnimationUtils.loadAnimation(this,R.anim.to_bottom_fab)
    }

    private val rotateclock:Animation by lazy{
        AnimationUtils.loadAnimation(this,R.anim.rotate_clock_wise)
    }

    private val anticlockwise:Animation by lazy{
        AnimationUtils.loadAnimation(this,R.anim.rotate_anticlock_wise)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentcon=findViewById(R.id.fragmentcon)
        budgettv = findViewById(R.id.budgettv)
        budgetfab = findViewById(R.id.budgetfab)
        guestfab = findViewById(R.id.guestfab)
        vendorfab = findViewById(R.id.vendorfab)
        navView= findViewById(R.id.navView)
        taskfab = findViewById(R.id.taskfab)
        guesttv = findViewById(R.id.guesttv)
        vendortv = findViewById(R.id.vendortv)
        tasktv = findViewById(R.id.TaskList)
        drawerLayout=findViewById(R.id.drawerlayout)
        addbutton=findViewById(R.id.addbutton)


        bottomnav=findViewById(R.id.bottomnav)
        bottomnav.background=null
        bottomnav.menu.getItem(2).isEnabled=false
        navigationDrawershow()

        bottomNavigationclickListener()
        addbutton.setOnClickListener{
            if(isExpanded){
                shrinkfab()
            }
            else{
                expand()
            }

        } }

    private fun bottomNavigationclickListener() {
        val bugetfrag= BudgetFragment()
        val vendorfrag=VendorFragment()
        val guestfrag=GuestFragment()
        bottomnav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {

                    true
                }
                R.id.budget -> {
//                    Intent(this,Budget::class.java).also {
//                        startActivity(it)
//                    }
                    currentfragment(bugetfrag)

                    true
                }
                R.id.vendor -> {
//                    Intent(this,Budget::class.java).also {
//                        startActivity(it)
//                    }
                    currentfragment(vendorfrag)

                    true
                }
                R.id.guest -> {
//                    Intent(this,Budget::class.java).also {
//                        startActivity(it)
//                    }
                    currentfragment(guestfrag)

                    true
                }

                else -> false
            }
        }
        budgetfab.setOnClickListener {
            Intent(this, BudgetDetails::class.java).also {
                startActivity(it)
            }
        }
        guestfab.setOnClickListener {
            Intent(this, GuestDetails::class.java).also {
                startActivity(it)
            }}

        vendorfab.setOnClickListener {
            Intent(this,VendorDetails::class.java).also { startActivity(it) }
        }
        taskfab.setOnClickListener {
            Intent(this,TaskDetails::class.java).also { startActivity(it) }
        }
    }

    //Navigation Drawer function
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

        drawerLayout.addDrawerListener(object :DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                addbutton.hide() // Hide the FAB when the drawer is opened
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                addbutton.show() // Show the FAB when the drawer is closed
            }
        })

    }

    //Floating action button on click expand function
    private fun expand() {
        //It is used when a user click on Floating action button all the all and textview appear with given amimation
        addbutton.startAnimation(rotateclock)
        budgetfab.startAnimation(frombottomfabanim)
        guestfab.startAnimation(frombottomfabanim)
        vendorfab.startAnimation(frombottomfabanim)
        taskfab.startAnimation(frombottomfabanim)
        budgettv.startAnimation(frombottomfabanim)
        vendortv.startAnimation(frombottomfabanim)
        guesttv.startAnimation(frombottomfabanim)
        tasktv.startAnimation(frombottomfabanim)
        isExpanded = !isExpanded


    }

    override fun onBackPressed() {
        if (isExpanded) {
            shrinkfab()
        } else {
            super.onBackPressed()

        }
    }

    //Floating action button on click Close function
    private fun shrinkfab() {
        //It is used when a user click on Floating action button all the all and textview disappear with given amimation
        addbutton.startAnimation(anticlockwise)
        budgetfab.startAnimation(tobottomfabanim)
        guestfab.startAnimation(tobottomfabanim)
        vendorfab.startAnimation(tobottomfabanim)
        taskfab.startAnimation(tobottomfabanim)
        budgettv.startAnimation(tobottomfabanim)
        vendortv.startAnimation(tobottomfabanim)
        guesttv.startAnimation(tobottomfabanim)
        tasktv.startAnimation(tobottomfabanim)
        isExpanded = !isExpanded
    }
    private fun currentfragment(fragment:Fragment) =
        supportFragmentManager.beginTransaction().apply {
        replace(R.id.fragmentcon,fragment)
            commit()
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//    if(toogle.onOptionsItemSelected(item)){
//        return true
//    }
//    return super.onOptionsItemSelected(item)
//    }
}