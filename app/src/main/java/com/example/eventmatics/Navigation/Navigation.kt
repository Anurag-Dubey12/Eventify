package com.example.eventmatics.Navigation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.eventmatics.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Navigation : AppCompatActivity() {
//    lateinit var drawerLayout: DrawerLayout
//    lateinit var navView: NavigationView
//    lateinit var toogle: ActionBarDrawerToggle
//    private lateinit var addbutton: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
//        navView = findViewById(R.id.navView)
//        drawerLayout=findViewById(R.id.drawerlayout)
//        addbutton=findViewById(R.id.addbutton)
//        val bottomnav: BottomNavigationView=findViewById(R.id.bottomnav)
//        bottomnav.background=null
//        bottomnav.menu.getItem(2).isEnabled=false
//        toogle= ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
//        drawerLayout.addDrawerListener(toogle)
//        toogle.syncState()
//
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        navView.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.item1 -> {
//                    Toast.makeText(applicationContext, "You clicked item 1", Toast.LENGTH_SHORT).show()
//                    drawerLayout.closeDrawers() // Close the navigation drawer
//                    true
//                }
//                else -> false
//            }
//        }
//        drawerLayout.addDrawerListener(object :DrawerLayout.SimpleDrawerListener(){
//            override fun onDrawerOpened(drawerView: View) {
//                super.onDrawerOpened(drawerView)
//                addbutton.hide() // Hide the FAB when the drawer is opened
//            }
//
//            override fun onDrawerClosed(drawerView: View) {
//                super.onDrawerClosed(drawerView)
//                addbutton.show() // Show the FAB when the drawer is closed
//            }
//        })

    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(toogle.onOptionsItemSelected(item)){
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}