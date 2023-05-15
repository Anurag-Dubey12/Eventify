package com.example.eventmatics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomnav: BottomNavigationView =findViewById(R.id.bottomnav)
        bottomnav.background=null
        bottomnav.menu.getItem(2).isEnabled=false


    }
}