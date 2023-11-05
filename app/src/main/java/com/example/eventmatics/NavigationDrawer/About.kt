package com.example.eventmatics.NavigationDrawer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.eventmatics.R

class About : AppCompatActivity() {
    lateinit var github_layout:LinearLayout
    lateinit var linkedin_layout:LinearLayout
    lateinit var instagram_layout:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        github_layout=findViewById(R.id.github_layout)
        linkedin_layout=findViewById(R.id.linkedin_layout)
        instagram_layout=findViewById(R.id.instagram_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        instagram_layout.setOnClickListener { OpenUrl("https://www.instagram.com/_itz_anurag_12/") }
        github_layout.setOnClickListener { OpenUrl("https://github.com/Anurag-Dubey12") }
        linkedin_layout.setOnClickListener { OpenUrl("https://www.linkedin.com/in/anurag-dubey-68720b247") }
    }
    private fun OpenUrl(Url:String){
        val uri:Uri=Uri.parse(Url)
        val browserIntent=Intent(Intent.ACTION_VIEW,uri)
        val packageManager = packageManager
        val activity=packageManager.queryIntentActivities(browserIntent,PackageManager.MATCH_DEFAULT_ONLY)
        if(activity.isNullOrEmpty()){ startActivity(browserIntent) }
        else{ startActivity(Intent(Intent.ACTION_VIEW,uri)) }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            else->super.onOptionsItemSelected(item)
        } } }