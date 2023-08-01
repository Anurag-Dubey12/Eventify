package com.example.eventmatics.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.getSharedPreference
import com.google.android.material.bottomsheet.BottomSheetDialog


class DatabaseNameHolder(context: Context) : BottomSheetDialog(context) {
    private lateinit var databaseRecycler: RecyclerView
    private lateinit var adapter: EventDatabaseAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_all_database)
        databaseRecycler = findViewById(R.id.DatabaseRecycler)!!
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databasename = getSharedPreference(context, "databasename").toString()
                val Databasenames = NamesDatabase(context)
                val DatabaseNames=Databasenames.getAllEventNames()
                adapter= EventDatabaseAdapter(DatabaseNames)
                databaseRecycler.adapter=adapter
                databaseRecycler.layoutManager=LinearLayoutManager(context)
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing=false
            },3000)
        }
//        EventNameInflate()
        showEventData()
    }

fun showEventData() {
    val databasename = getSharedPreference(context, "databasename").toString()
    val Databasenames = NamesDatabase(context)
    val DatabaseNames=Databasenames.getAllEventNames()
    adapter= EventDatabaseAdapter(DatabaseNames)
    databaseRecycler.adapter=adapter
    databaseRecycler.layoutManager=LinearLayoutManager(context)
    adapter.notifyDataSetChanged()

}
    fun getsharedpreference(context: Context,key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }

}
