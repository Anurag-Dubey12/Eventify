package com.example.eventmatics.NavigationDrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.AuthenticationUid
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.google.android.material.appbar.MaterialToolbar

class EventList : AppCompatActivity() {
    private lateinit var  databaseRecycler:RecyclerView
    private lateinit var adapter: EventDatabaseAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        databaseRecycler = findViewById(R.id.DatabaseRecycler)!!
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)!!

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        adapter = EventDatabaseAdapter(this, emptyList(),{ position, ->
        }) {name->
            DatabaseManager.changeDatabaseName(this,name)
            Toast.makeText(this,"Event Change to $name",Toast.LENGTH_SHORT).show()
        }

        databaseRecycler.adapter = adapter
        databaseRecycler.layoutManager = LinearLayoutManager(this)
        showEventData()
    }

    private fun showEventData() {
        try {
//            val databasenames =DatabaseManager.getDatabase(this)
            val databasenames = NamesDatabase(this)
            val uid=AuthenticationUid.getUserUid(this)
            Log.d("UerUid","This is EVentList userid:$uid")
            val databaseNames = databasenames.getAllEventNamesWithoutDuplicatesForUser(uid.toString())
            adapter.updateData(databaseNames)
        }
        catch (e:Exception){
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("Error: ","${e.message}")
        }
    }

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
}