package com.example.eventmatics.NavigationDrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmatics.RoomDatabase.Dao.NamesDatabaseDao
import com.example.eventmatics.RoomDatabase.AuthenticationUid
import com.example.eventmatics.RoomDatabase.DataClas.DatabaseNameDataClass
import com.example.eventmatics.RoomDatabase.DataClas.EventEntity
import com.example.eventmatics.RoomDatabase.NamesDatabase
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventList : AppCompatActivity(),EventDatabaseAdapter.OnItemClickListener {
    private lateinit var databaseRecycler: RecyclerView
    private lateinit var adapter: EventDatabaseAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var dbname: NamesDatabase? = null
    private var namesdao: NamesDatabaseDao? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        databaseRecycler = findViewById(R.id.DatabaseRecycler)!!
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)!!

        swipeRefreshLayout.setOnRefreshListener { GlobalScope.launch(Dispatchers.IO) {refreshData() }}
        adapter = EventDatabaseAdapter(this, emptyList(), { name ->
            GlobalScope.launch(Dispatchers.IO) {
                RoomDatabaseManager.changeDatabaseName(applicationContext, name)
                val rootView = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar.make(rootView!!, "Database Changed", Snackbar.LENGTH_SHORT)
                val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.BOTTOM
                snackbar.view.layoutParams = params
                snackbar.show()
            }
        }, this)
        databaseRecycler.adapter = adapter
        databaseRecycler.layoutManager = LinearLayoutManager(this)
        showEventData()
    }

    private fun showEventData() {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                dbname = NamesDatabase.createDatabase(applicationContext)
                namesdao = dbname?.namesdatabasedao()
                val uid = AuthenticationUid.getUserUid(applicationContext)
                Log.d("UerUid", "This is EVentList userid:$uid")
                val databaseNames =
                    namesdao?.getAllEventNamesWithoutDuplicatesForUser(uid.toString())
                adapter.updateData(databaseNames!!)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refreshData() {
        runOnUiThread {
            swipeRefreshLayout.isRefreshing = true
        }
        GlobalScope.launch(Dispatchers.IO) {
            Thread.sleep(2000)

            runOnUiThread {
                Handler().postDelayed({
                    showEventData()
                    swipeRefreshLayout.isRefreshing = false
                }, 1000)
            }
        }
    }



    override fun onItemClick(DatabaseList: DatabaseNameDataClass) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val dao = RoomDatabaseManager.getEventsDao(applicationContext)
                dbname = NamesDatabase.createDatabase(applicationContext)
                namesdao = dbname?.namesdatabasedao()

                runOnUiThread {
                    val view = layoutInflater.inflate(R.layout.event_delete, null)
                    val dialog = MaterialAlertDialogBuilder(applicationContext)
                        .setView(view)
                        .create()

                    val btn_okay: Button = view.findViewById(R.id.btn_okay)
                    val btn_cancel: Button = view.findViewById(R.id.btn_cancel)
                    val msgtext: TextView = view.findViewById(R.id.msgtext)

                    msgtext.text =
                        "Do you want to delete ${DatabaseList.DatabaseName} scheduled for ${DatabaseList.Date} at ${DatabaseList.Time}?"
                    btn_cancel.setOnClickListener { dialog.dismiss() }
                    btn_okay.setOnClickListener {
                        val eventlist = EventEntity(
                            DatabaseList.id,
                            DatabaseList.DatabaseName,
                            DatabaseList.Date,
                            DatabaseList.Time,
                            DatabaseList.Budget,
                            DatabaseList.uid
                        )
                        dao.deleteEvent(eventlist)
                        namesdao?.deleteEventName(DatabaseList)
                        this@EventList.deleteDatabase(DatabaseList.DatabaseName)
                        swipeRefreshLayout.isRefreshing = true
                        Handler().postDelayed({
                            showEventData()
                            swipeRefreshLayout.isRefreshing = false
                        }, 1000)
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("delete_Eventlist", "Event Could Not Be deleted Because:${e.message}")
        }
    }
}