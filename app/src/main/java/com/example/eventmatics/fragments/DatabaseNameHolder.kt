package com.example.eventmatics.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Toast
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
                adapter= EventDatabaseAdapter(context,DatabaseNames){position ->
                    ChangeDatabase(position)
                }
                databaseRecycler.adapter=adapter
                databaseRecycler.layoutManager=LinearLayoutManager(context)
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing=false
            },3000)
        }
//        EventNameInflate()
        showEventData()
    }
    interface DatabaseChangeListener {
        fun onDatabaseChanged(newDatabaseName: String)
    }
    private var databaseChangeListener: DatabaseChangeListener? = null
    fun setDatabaseChangeListener(listener: DatabaseChangeListener?) {
        this.databaseChangeListener = listener
    }


    fun showEventData() {
    val databasename = getSharedPreference(context, "databasename").toString()
    val Databasenames = NamesDatabase(context)
    val DatabaseNames=Databasenames.getAllEventNames()
    adapter= EventDatabaseAdapter(context,DatabaseNames){position ->
        ChangeDatabase(position)
    }
    databaseRecycler.adapter=adapter
    databaseRecycler.layoutManager=LinearLayoutManager(context)
    adapter.notifyDataSetChanged()

}
    fun getsharedpreference(context: Context,key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }
//    private fun ChangeDatabase(position:Int) {
//        try{
//            val sharedvalue=context.getSharedPreferences("Database", Context.MODE_PRIVATE)
////          val databasename = getSharedPreference(context, "databasename").toString()
//            val db = NamesDatabase(context)
//            val Names=db.GetNamesEventsData(position)
//            val DatabaseName=Names?.DatabaseName
//            Log.d("Database_Names","Database name is:$Names")
//            val editor=sharedvalue.edit()
//            if(sharedvalue.contains("Database")){
//                editor.remove("Database")
//            }
//            editor.putString("Database",DatabaseName)
//            editor.apply()
//            Log.d("Database_Names","Shared value is :$sharedvalue")
//            databaseChangeListener?.onDatabaseChanged(newDatabaseName ?: "")
//
//            dismiss()
//
//        }catch (e:Exception){
//            e.printStackTrace()
//            Toast.makeText(context,"Database Could Not Be Changed Because :${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
private fun ChangeDatabase(position: Int) {
    try {
        val sharedvalue = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val db = NamesDatabase(context)
        val Names = db.GetNamesEventsData(position)
        val DatabaseName = Names?.DatabaseName
        Log.d("Database_Names", "Database name is:$Names")
        val editor = sharedvalue.edit()
        if (sharedvalue.contains("Database")) {
            editor.remove("Database")
        }
        editor.putString("Database", DatabaseName)
        editor.apply()
        Log.d("Database_Names", "Shared value is :$sharedvalue")

        // Invoke the callback here to notify the MainActivity of the database change
        databaseChangeListener?.onDatabaseChanged(DatabaseName ?: "")

        dismiss()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Database Could Not Be Changed Because :${e.message}", Toast.LENGTH_SHORT).show()
    }
}

}
