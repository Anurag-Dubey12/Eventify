package com.example.eventmatics.Navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatics.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Navigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val bottomnav: BottomNavigationView=findViewById(R.id.bottomnav)
        bottomnav.background=null
        bottomnav.menu.getItem(2).isEnabled=false

    }
}