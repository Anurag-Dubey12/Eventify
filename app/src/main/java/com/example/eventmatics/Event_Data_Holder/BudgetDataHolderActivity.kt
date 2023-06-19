package com.example.eventmatics.Event_Data_Holder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.example.eventmatics.data_class.BudgetDataHolderData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BudgetDataHolderActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    lateinit var budgetAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:BudgetDataHolderAdapter
    lateinit var budgetlist:MutableList<BudgetDataHolderData>
    private var filteredList: MutableList<BudgetDataHolderData> = mutableListOf()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_data_holder)
        recyclerView = findViewById(R.id.BudgetDatarec)
        budgetAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val budgetDetails=BudgetDetails()
        budgetlist= mutableListOf()
//        adapter= BudgetDataHolderAdapter( budgetlist)
        recyclerView.layoutManager=LinearLayoutManager(this)
//        recyclerView.adapter=adapter

        val dataitem=intent.getParcelableArrayListExtra<BudgetDataHolderData>("dataitem")
        if(dataitem!=null){
            adapter=BudgetDataHolderAdapter(dataitem)
            recyclerView.adapter=adapter
        }
//
//        budgetlist.add(BudgetDataHolderData("ANurag","155", 2000F,"200","Paid"))
//        budgetlist.add(BudgetDataHolderData("burag","155",200f,"200","Paid"))
//        budgetlist.add(BudgetDataHolderData("ANurag","155",100f,"200","Pending"))
//        budgetlist.add(BudgetDataHolderData("ANurag","155",4000f,"200","Pending"))
//        filteredList.addAll(budgetlist)

        budgetAdd.setOnClickListener {
            Intent(this,BudgetDetails::class.java).also { startActivity(it) }
        }
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

        pendingOnly.setOnClickListener {
            filteredList.clear()
            filteredList.addAll(budgetlist.filter { it.traninfo == "Pending" })
            adapter.updateList(filteredList)
            dialog.dismiss()
        }

        paidOnly.setOnClickListener {
            filteredList.clear()
            filteredList.addAll(budgetlist.filter { it.traninfo == "Paid" })
            adapter.updateList(filteredList)
            dialog.dismiss()
        }
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
            budgetlist.sortBy { it.eventName }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        nameDescending.setOnClickListener {
            budgetlist.sortByDescending { it.eventName }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        amountAscending.setOnClickListener {
            budgetlist.sortBy { it.amount }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        amountDescending.setOnClickListener {
            budgetlist.sortByDescending { it.amount }
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
    }
}