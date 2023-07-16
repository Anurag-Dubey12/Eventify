package com.example.eventmatics.Events_Data_Holder_Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BudgetDataHolderActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    lateinit var budgetAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:BudgetDataHolderAdapter
    lateinit var budgetlist:MutableList<Budget>
    private var filteredList: MutableList<Budget> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_data_holder)
        recyclerView = findViewById(R.id.BudgetDatarec)
        budgetAdd=findViewById(R.id.fab)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        budgetlist= mutableListOf()

        budgetAdd.setOnClickListener {
            Intent(this,BudgetDetails::class.java).also { startActivity(it) }
        }

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databasename=getSharedPreference(this,"databasename").toString()
                val databasehelper = LocalDatabase(this, databasename)
                val BudgetList = databasehelper.getAllBudgets()
                if(BudgetList!=null){
                    adapter = BudgetDataHolderAdapter(BudgetList)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)
            }
                swipeRefreshLayout.isRefreshing=false

        },3000)
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.Coral,
            R.color.Fuchsia,
            R.color.Indigo
        )
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Lemon_Chiffon)
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150)
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    showSortOptions()
                    true
                }
                R.id.Filter -> {
                    showFilterOptions()
                    true
                }
                else -> false
            }
        }
        showbudgetlist()
    }
    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
    private fun showbudgetlist() {
        val databasename=getSharedPreference(this,"databasename").toString()
        val databasehelper = LocalDatabase(this, databasename)
        val BudgetList = databasehelper.getAllBudgets()
        if(BudgetList!=null){
            adapter = BudgetDataHolderAdapter(BudgetList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onResume() {
        super.onResume()
        val databasename=getSharedPreference(this,"databasename").toString()
        val databasehelper = LocalDatabase(this, databasename)
        val BudgetList = databasehelper.getAllBudgets()
        if(BudgetList!=null){
            adapter = BudgetDataHolderAdapter(BudgetList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showFilterOptions() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.filterpopup, null)

        dialogBuilder.setView(view)
        dialogBuilder.setTitle("Select List To Filter")

        val showAll = view.findViewById<TextView>(R.id.show_all)
        val pendingOnly = view.findViewById<TextView>(R.id.Pending_only)
        val paidOnly = view.findViewById<TextView>(R.id.Paid_only)

        val dialog = dialogBuilder.create()
        dialog.show()


        showAll.setOnClickListener {
            filteredList.clear()
            filteredList.addAll(budgetlist)
            adapter.updateList(filteredList)
            dialog.dismiss()
        }

//        pendingOnly.setOnClickListener {
//            filteredList.clear()
//            filteredList.addAll(budgetlist.filter { it.traninfo == "Pending" })
//            adapter.updateList(filteredList)
//            dialog.dismiss()
//        }
//
//        paidOnly.setOnClickListener {
//            filteredList.clear()
//            filteredList.addAll(budgetlist.filter { it.traninfo == "Paid" })
//            adapter.updateList(filteredList)
//            dialog.dismiss()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
    private fun showSortOptions() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.sortpopup, null)

        dialogBuilder.setView(view)

        val nameAscending = view.findViewById<TextView>(R.id.name_ascen)
        val nameDescending = view.findViewById<TextView>(R.id.name_decen)
        val amountAscending = view.findViewById<TextView>(R.id.Amount_ascen)
        val amountDescending = view.findViewById<TextView>(R.id.Amount_decen)

        dialogBuilder.setTitle("Select list order type")
        val dialog = dialogBuilder.create()
        dialog.show()

        nameAscending.setOnClickListener {
            budgetlist.sortBy { it.name }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        nameDescending.setOnClickListener {
            budgetlist.sortByDescending { it.name }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        amountAscending.setOnClickListener {
            budgetlist.sortBy { it.estimatedAmount }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        amountDescending.setOnClickListener {
            budgetlist.sortByDescending { it.estimatedAmount }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
    }
}