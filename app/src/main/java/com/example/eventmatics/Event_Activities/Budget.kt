package com.example.eventmatics.Event_Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatics.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Budget : AppCompatActivity(R.layout.activity_budget) {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)
    }
}