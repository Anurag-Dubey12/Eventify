package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.getSharedPreference

class EventList : AppCompatActivity() {
    private lateinit var  databaseRecycler:RecyclerView
    private lateinit var adapter: EventDatabaseAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        databaseRecycler = findViewById(R.id.DatabaseRecycler)!!
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)!!

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        adapter = EventDatabaseAdapter(this, emptyList(),{ position, ->
        }) {name->
            DatabaseManager.changeDatabaseName(this,name)
        }

        databaseRecycler.adapter = adapter
        databaseRecycler.layoutManager = LinearLayoutManager(this)
        showEventData()
    }

    private fun showEventData() {
        try {
            val databasenames = NamesDatabase(this)
            val databaseNames = databasenames.getAllEventNames()
            adapter.updateData(databaseNames)
        }
        catch (e:Exception){
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            Log.d("Error: ","${e.message}")
        }
    }


    private fun refreshData() {
        // Simulate data refreshing with a delay
        Handler().postDelayed({
            val databasename = getSharedPreference(this, "databasename").toString()
            val databasenames = NamesDatabase(this)
            val databaseNames = databasenames.getAllEventNames()

            // Update the adapter data with the refreshed database names
            adapter.updateData(databaseNames)

            swipeRefreshLayout.isRefreshing = false
        }, 3000)
    }
}