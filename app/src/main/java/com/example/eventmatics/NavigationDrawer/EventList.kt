package com.example.eventmatics.NavigationDrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmatics.Adapter.EventLayoutAdapter
import com.example.eventmatics.SQLiteDatabase.Dataclass.AuthenticationUid
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.DatabaseNameDataClass
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Events
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EventList : AppCompatActivity(),EventDatabaseAdapter.OnItemClickListener {
    private lateinit var  databaseRecycler:RecyclerView
    private lateinit var adapter: EventDatabaseAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private  var EventList:MutableList<Events> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        databaseRecycler = findViewById(R.id.DatabaseRecycler)!!
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)!!

        swipeRefreshLayout.setOnRefreshListener { refreshData() }
        adapter = EventDatabaseAdapter(this, emptyList(),{name->
            DatabaseManager.changeDatabaseName(this,name)
            Toast.makeText(this,"Event Change to $name",Toast.LENGTH_SHORT).show()
        },this)
        databaseRecycler.adapter = adapter
        databaseRecycler.layoutManager = LinearLayoutManager(this)
        showEventData()
    }

    private fun showEventData() {
        try {
            val databasenames = NamesDatabase(this)
            val uid=AuthenticationUid.getUserUid(this)
            Log.d("UerUid","This is EVentList userid:$uid")
            val databaseNames = databasenames.getAllEventNamesWithoutDuplicatesForUser(uid.toString())
            adapter.updateData(databaseNames)
        }
        catch (e:Exception){
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        } }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            else->super.onOptionsItemSelected(item)
        }}

    private fun refreshData() {
        Handler().postDelayed({
            val databasenames = NamesDatabase(this)
            val uid=AuthenticationUid.getUserUid(this)
            val databaseNames = databasenames.getAllEventNamesWithoutDuplicatesForUser(uid.toString())
            adapter.updateData(databaseNames)
            swipeRefreshLayout.isRefreshing = false
        }, 1000)
    }

    override fun onItemClick(DatabaseList: DatabaseNameDataClass) {
        try{
            val id=DatabaseList.id
            val dbname = NamesDatabase(this)
            val db=DatabaseManager.getDatabase(this)
            val view=layoutInflater.inflate(R.layout.event_delete,null)
            val dialog=MaterialAlertDialogBuilder(this)
                .setView(view)
                .create()
            val btn_okay:Button=view.findViewById(R.id.btn_okay)
            val btn_cancel:Button=view.findViewById(R.id.btn_cancel)
            val msgtext:TextView=view.findViewById(R.id.msgtext)
            msgtext.text = "Do you want to delete ${DatabaseList.DatabaseName} scheduled for ${DatabaseList.Date} at ${DatabaseList.Time}?"
            btn_cancel.setOnClickListener { dialog.dismiss() }
            btn_okay.setOnClickListener {
                val eventlist= Events(DatabaseList.id,DatabaseList.DatabaseName,DatabaseList.Date,DatabaseList.Time,DatabaseList.Budget,DatabaseList.uid)
                db.deleteEvent(eventlist)
                dbname.deleteEventName(DatabaseList)
                this.deleteDatabase(DatabaseList.DatabaseName)
                Toast.makeText(this,"Event Deteled Successfully",Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing=true
                Handler().postDelayed({
                    val databasenames = NamesDatabase(this)
                    val uid=AuthenticationUid.getUserUid(this)
                    val databaseNames = databasenames.getAllEventNamesWithoutDuplicatesForUser(uid.toString())
                    adapter.updateData(databaseNames)
                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                }, 1000)
                dialog.dismiss()
            }
            dialog.show()
        } catch (e:Exception){
            e.printStackTrace()
            Log.d("detele_Eventlist","Event Could Not Be deteled Because:${e.message}")
        }
    } }