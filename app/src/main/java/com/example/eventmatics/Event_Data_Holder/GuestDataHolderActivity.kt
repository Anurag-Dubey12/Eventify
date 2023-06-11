package com.example.eventmatics.Event_Data_Holder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Event_Details_Activity.GuestDetails
import com.example.eventmatics.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GuestDataHolderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var guestAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_data_holder)
        recyclerView = findViewById(R.id.guestDatarec)
        guestAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        guestAdd.setOnClickListener {
            Intent(this,GuestDetails::class.java).also { startActivity(it) }
        }
    }
}