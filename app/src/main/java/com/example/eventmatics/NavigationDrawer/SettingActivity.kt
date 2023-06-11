package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.eventmatics.R

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar: Toolbar =findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val themegroup:RadioGroup=findViewById(R.id.themeRadioGroup)
        themegroup.setOnCheckedChangeListener { _, checkedId ->
            val theme:RadioButton=findViewById(checkedId)
            val text=theme.text.toString()

            if(text=="Dark Theme"){
                setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
           else{
                setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }
    private fun setAppTheme(mode:Int){
        AppCompatDelegate.setDefaultNightMode(mode)
        recreate()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }else-> super.onOptionsItemSelected(item)
        }
    }
}