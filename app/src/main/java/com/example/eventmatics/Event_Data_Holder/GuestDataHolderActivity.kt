package com.example.eventmatics.Event_Data_Holder

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Event_Details_Activity.GuestDetails
import com.example.eventmatics.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GuestDataHolderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var guestAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_data_holder)
        recyclerView = findViewById(R.id.guestDatarec)
        guestAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        guestAdd.setOnClickListener {
            Intent(this,GuestDetails::class.java).also { startActivity(it) }
        }
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    showSortOptions()
                    true
                }

                else -> false
            }
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
//            budgetlist.sortBy { it.eventName }
//            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        nameDescending.setOnClickListener {
//            budgetlist.sortByDescending { it.eventName }
//            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        amountAscending.setOnClickListener {
//            budgetlist.sortBy { it.amount }
//            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        amountDescending.setOnClickListener {
//            budgetlist.sortByDescending { it.amount }
//            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }

            else->super.onOptionsItemSelected(item)
        }
    }
}