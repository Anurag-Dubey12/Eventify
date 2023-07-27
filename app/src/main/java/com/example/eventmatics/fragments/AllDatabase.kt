package com.example.eventmatics.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.getSharedPreference


class AllDatabase(context: Context) : AppCompatDialog(context) {
    private lateinit var databaseRecycler: RecyclerView
    private lateinit var adapter: EventDatabaseAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var countDownTimer:CountDownTimer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_all_database)
//        databaseRecycler = findViewById(R.id.DatabaseRecycler)!!
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)!!
//        swipeRefreshLayout.setOnRefreshListener {
//            Handler().postDelayed({
//                val databasename=getsharedpreference(context,"databasename").toString()
//                val db= DatabaseAdapter(context,databasename)
//                val EventNameList=db.getAllEventNames()
//                if(EventNameList!=null){
//                    adapter=EventDatabaseAdapter(EventNameList)
//                    databaseRecycler.adapter=adapter
//                    databaseRecycler.layoutManager=LinearLayoutManager(context)
//                }
//                swipeRefreshLayout.isRefreshing=false
//            },3000)
//        }
//        EventNameInflate()
    }

//    private fun EventNameInflate() {
//        val databasename=getsharedpreference(context,"databasename").toString()
//        val db= DatabaseAdapter(context,databasename)
//        val EventNameList=db.getAllEventNames()
//        if(EventNameList!=null){
//            adapter=EventDatabaseAdapter(EventNameList)
//            databaseRecycler.adapter=adapter
//            databaseRecycler.layoutManager=LinearLayoutManager(context)
//        }
//    }
fun showEventData() {
    val databasename = getSharedPreference(context, "databasename").toString()
    val databasehelper = LocalDatabase(context, databasename)
    val DatabaseList = databasehelper.getAllEvents()
//    val adpater=EventDatabaseAdapter(DatabaseList)
//    databaseRecycler.adapter = adapter
//    databaseRecycler.layoutManager = LinearLayoutManager(context)

    adapter.notifyDataSetChanged()
}
    fun getsharedpreference(context: Context,key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }

}
