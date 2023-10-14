package com.example.eventmatics.Events_Data_Holder_Activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BudgetDataHolderActivity : AppCompatActivity(),BudgetDataHolderAdapter.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    lateinit var budgetAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:BudgetDataHolderAdapter
    lateinit var budgetlist:MutableList<Budget>
    private lateinit var Data_Not_found: ImageView
    lateinit var bmp:Bitmap
    lateinit var scalebmp:Bitmap
    private var isRecyclerViewEmpty = true
    var PERMISSION_CODE=101
    private var filteredList: MutableList<Budget> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onItemClick(budget: Budget) {
        Intent(this, BudgetDetails::class.java).apply {
            putExtra("selected_item", budget)
            startActivity(this)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_data_holder)
        recyclerView = findViewById(R.id.BudgetDatarec)
        Data_Not_found = findViewById(R.id.data_not_found)
        budgetAdd=findViewById(R.id.fab)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(recyclerView.adapter?.itemCount==0){
            Data_Not_found.visibility= View.VISIBLE
        }else{
            Data_Not_found.visibility= View.GONE
        }
        budgetlist= mutableListOf()

        budgetAdd.setOnClickListener {
            Intent(this,BudgetDetails::class.java).also { startActivity(it) }
        }

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val db = DatabaseManager.getDatabase(this)
                val BudgetList = db.getAllBudgets()
                isRecyclerViewEmpty=BudgetList.isNullOrEmpty()
                if(BudgetList!=null){
                    adapter = BudgetDataHolderAdapter(this,BudgetList,this)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this)
            }
                swipeRefreshLayout.isRefreshing=false
//                removeSharedPreference(this, "databasename")


            },1)
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
        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled( recyclerView, dx, dy)
                val isAtTop=recyclerView.canScrollVertically(-1)

                swipeRefreshLayout.isEnabled=!isAtTop

            }
        })
        showbudgetlist()
    }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val pdfReportItem = menu.findItem(R.id.pdfreport)
        val check = menu.findItem(R.id.Check)
        pdfReportItem.isVisible = !isRecyclerViewEmpty
        return true
    }

    private fun showbudgetlist() {
        val db = DatabaseManager.getDatabase(this)
        val BudgetList = db.getAllBudgets()
        isRecyclerViewEmpty=BudgetList.isNullOrEmpty()
        if(BudgetList!=null){
            adapter = BudgetDataHolderAdapter(this,BudgetList,this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        val swipe=object:BudgetSwipeToDelete(this){
            var deleteditem: Budget? = null
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position=viewHolder.adapterPosition
                val newPaidStatus:String
                var previousPaidStatus:String
                var NewBalance:Float
                val bal=0;
                when(direction){
                    ItemTouchHelper.LEFT->{
                if(position!=RecyclerView.NO_POSITION){
                    deleteditem=BudgetList[position]

                    adapter.notifyItemRemoved(position)

                    MaterialAlertDialogBuilder(this@BudgetDataHolderActivity)
                        .setTitle("Delete Budget Item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Delete"){dialog,_->
                            db.deleteBudget(deleteditem!!)
                            recreate()
                        }
                        .setNegativeButton("Cancel"){dialog,_->
                            dialog.dismiss()
                            recreate()
                        }
                        .show()
                }
                    }
                    ItemTouchHelper.RIGHT->{
                        if(position!=RecyclerView.NO_POSITION){
                            val budget = BudgetList[position]
                            adapter.notifyItemRemoved(position)
                            // Get the current 'Paid' status of the budget
                            val currentPaidStatus = budget.paid
                            val budgetCost=budget.estimatedAmount

                            // Determine the new 'Paid' status based on the current status
                             if (currentPaidStatus == "Paid"){
                                 newPaidStatus="Not Paid"
                                 NewBalance= budget.estimatedAmount.toFloat()
                             } else {
                                 newPaidStatus="Paid"
                                 NewBalance=bal.toFloat()
                             }
                            when(currentPaidStatus){
                                "Paid"->{
                                    MaterialAlertDialogBuilder(this@BudgetDataHolderActivity)
                                        .setTitle("Budget Payment")
                                        .setMessage("Is the Status of the Budget Payment is Not Paid?")
                                        .setPositiveButton("Not Paid"){dialog,_->
                                            db.updateBudgetPaid(budget.id, newPaidStatus,"$NewBalance")
                                            recreate()
                                        }
                                        .setNeutralButton("Cancel"){dialog,_->
                                            dialog.dismiss()
                                            recreate()
                                        }
                                        .show()
                                }
                                "Not Paid"->{
                                    MaterialAlertDialogBuilder(this@BudgetDataHolderActivity)
                                        .setTitle("Budget Payment")
                                        .setMessage("Is the Status of the Budget Payment is Paid?")
                                        .setPositiveButton("PAID"){dialog,_->
                                            db.updateBudgetPaid(budget.id, newPaidStatus,"$NewBalance")
                                            recreate()
                                        }
                                        .setNeutralButton("Cancel"){dialog,_->
                                            dialog.dismiss()
                                            recreate()
                                        }
                                        .show()
                                }
                            }
                    }

                }
            }
            }
        }
        val itemTouch=ItemTouchHelper(swipe)
        itemTouch.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        showbudgetlist()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.holder,menu)

        val searchitem=menu?.findItem(R.id.action_search)

        val searchView=searchitem?.actionView as androidx.appcompat.widget.SearchView
        searchView.queryHint="Search"

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchBudget(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                searchBudget(newText)
                return true
            }
        })
        return true
    }
    fun searchBudget(query:String){
        val db = DatabaseManager.getDatabase(this)
        val BudgetFilter=db.SearchBudget(query)
       adapter.setadapter(BudgetFilter)
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
      val SortValue= arrayOf("Alphabetic(A-Z)","Amount")
       val DilogShow= MaterialAlertDialogBuilder(this)
            .setTitle("Sort Data")
            .setSingleChoiceItems(SortValue,-1){dialog,which->
                when(SortValue[which]){
                    "Alphabetic(A-Z)"->{
                        budgetlist.sortBy { it.name }
                        adapter.notifyDataSetChanged()
                    }
                    "Amount"->{
                        budgetlist.sortBy { it.estimatedAmount }
                        adapter.notifyDataSetChanged()
                    }
                    }
                }
            .setNeutralButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
        DilogShow.show()
}
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    private fun showFilterOptions() {
      val FilterValue= arrayOf("Paid","Not Paid")
        MaterialAlertDialogBuilder(this)
            .setTitle("Filter Data")
            .setSingleChoiceItems(FilterValue,-1){dialog,which->
                val SelectedFilter=FilterValue[which]
                val FilterList=budgetlist.filter {
                    it.paid==SelectedFilter
                }
                adapter.setadapter(filteredList.toMutableList())
                dialog.dismiss()
            }
            .setNeutralButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .show()
}
}