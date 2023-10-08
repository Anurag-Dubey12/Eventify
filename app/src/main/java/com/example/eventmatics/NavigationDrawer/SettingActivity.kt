package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.shadow.ShadowRenderer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SettingActivity : AppCompatActivity() {
    private val sharedperfkey="Theme"
    private lateinit var Sharedpref:SharedPreferences
    private lateinit var LocalBackupbutton:MaterialButton
    private lateinit var resetDataButton:MaterialButton
//    private lateinit var Loadbackup:MaterialButton
    private val PICK_FILE_REQUEST = 1
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val toolbar: Toolbar =findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Sharedpref = getSharedPreferences(sharedperfkey, Context.MODE_PRIVATE)
        val current=Sharedpref.getInt(sharedperfkey,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setAppTheme(current)
        val themegroup:RadioGroup=findViewById(R.id.themeRadioGroup)
         LocalBackupbutton=findViewById(R.id.LocalBackupbutton)
        resetDataButton=findViewById(R.id.resetDataButton)
//        Loadbackup=findViewById(R.id.LoadBackup)


        val radiobut=when(current){
            AppCompatDelegate.MODE_NIGHT_YES->R.id.darkThemeRadioButton
            else->R.id.lightThemeRadioButton
        }
        themegroup.check(radiobut)
        themegroup.setOnCheckedChangeListener { _, checkedId ->
            val theme:RadioButton=findViewById(checkedId)
            val text=theme.text.toString()

            val selectedMode=when(text){
                "Dark Theme"->AppCompatDelegate.MODE_NIGHT_YES
                else->AppCompatDelegate.MODE_NIGHT_NO
            }
            setAppTheme(selectedMode)
            SaveTheme(selectedMode)
        }
        LocalBackupbutton.setOnClickListener {
            backupDatabase()
        }
        resetDataButton.setOnClickListener {
            ResetData()
        }
//        Loadbackup.setOnClickListener {
//            LoadBackupData()
//        }
    }
    fun LoadBackupData() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, PICK_FILE_REQUEST)
        } else {

        }
    }

    private fun ResetData() {

        val db = DatabaseManager.getDatabase(this)
        val Namesdb = NamesDatabase(this)
        db.deteleAllEvent()
        Namesdb.deleteAll()
        finish()
        Toast.makeText(this, "Data Reset Successfully", Toast.LENGTH_SHORT).show()
    }
    private fun backupDatabase() {
        try {

            val db =DatabaseManager.getDatabase(this)
            val currentpath = applicationContext.getDatabasePath(db.toString()).absolutePath
            Log.d("Path", "Database path is: $currentpath")
            val Event_Details = db.getEventData(1)
            val EventName = Event_Details?.name

            val ParentDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
            if (!ParentDirectory.exists()) {
                ParentDirectory.mkdirs()
            }
            val BackUpDirectory = File(ParentDirectory, "Backup")
            if (!BackUpDirectory.exists()) {
                BackUpDirectory.mkdirs()
            }
            val timestamp = System.currentTimeMillis()

            val PdfFilePath = File(BackUpDirectory, "$EventName$timestamp.db").absolutePath

            copyFile(File(currentpath), File(PdfFilePath))
            Toast.makeText(this, "Backup has been Created Successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Backup", "Backup Could Not Be Processed Because: ${e.message}")
        }
    }


    private fun copyFile(sourcefile: File, dest: File) {
        if (!sourcefile.exists()) return
        val source = FileInputStream(sourcefile).channel
        val destination = FileOutputStream(dest).channel

        source.transferTo(0, source.size(), destination)

        source.close()
        destination.close()
    }


    private fun setAppTheme(mode:Int){
        AppCompatDelegate.setDefaultNightMode(mode)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }else-> super.onOptionsItemSelected(item)
        }
    }

    fun SaveTheme(mode:Int){
        Sharedpref.edit().putInt(sharedperfkey,mode).apply()
    }
}