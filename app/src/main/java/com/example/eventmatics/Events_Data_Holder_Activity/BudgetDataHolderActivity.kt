package com.example.eventmatics.Events_Data_Holder_Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.BudgetEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BudgetDataHolderActivity : AppCompatActivity(),BudgetDataHolderAdapter.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    lateinit var budgetAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:BudgetDataHolderAdapter
    lateinit var budgetlist:MutableList<BudgetEntity>
    private lateinit var Data_Not_found: ImageView
    private var isRecyclerViewEmpty = true
    private var filteredList: MutableList<BudgetEntity> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onItemClick(budget: BudgetEntity) {
        Intent(this, BudgetDetails::class.java).apply {
            putExtra("selected_item", budget)
            startActivity(this)
        } }
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
        GlobalScope.launch(Dispatchers.Main){
            RoomDatabaseManager.initialize(applicationContext)

        }
        Data_Not_found.visibility = if (recyclerView.adapter?.itemCount == 0) View.VISIBLE else View.GONE
        budgetlist= mutableListOf()
        budgetAdd.setOnClickListener { Intent(this,BudgetDetails::class.java).also { startActivity(it) } }
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                showbudgetlist()
                swipeRefreshLayout.isRefreshing=false
            },1)
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.Coral, R.color.Fuchsia, R.color.Indigo)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Lemon_Chiffon)
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150)
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> { showSortOptions()
                    true
                }
                R.id.Filter -> { showFilterOptions()
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

    private fun showbudgetlist() {
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO){
        val BudgetList = dao.getAllBudgets()
        isRecyclerViewEmpty=BudgetList.isNullOrEmpty()
            runOnUiThread{
                if(BudgetList!=null){
                    adapter = BudgetDataHolderAdapter(applicationContext,BudgetList,this@BudgetDataHolderActivity)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@BudgetDataHolderActivity)
                }
        val swipe=object:BudgetSwipeToDelete(this@BudgetDataHolderActivity){
            var deleteditem: BudgetEntity? = null
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position=viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.LEFT->{
                if(position!=RecyclerView.NO_POSITION){
                    deleteditem=BudgetList[position]
                    adapter.notifyItemRemoved(position)
                    MaterialAlertDialogBuilder(this@BudgetDataHolderActivity)
                        .setTitle("Delete Budget Item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Delete"){dialog,_->
                            GlobalScope.launch(Dispatchers.IO){
                            dao.deleteBudget(deleteditem!!)
                            }
                            recreate()
                        }
                        .setNegativeButton("Cancel"){dialog,_->
                            dialog.dismiss()
                            recreate()
                        }.show()
                } } } } }
        val itemTouch=ItemTouchHelper(swipe)
        itemTouch.attachToRecyclerView(recyclerView)
    }
            }
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
            override fun onQueryTextSubmit(query: String): Boolean { searchBudget(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean { searchBudget(newText)
                return true
            }
        })
        return true
    }
    fun searchBudget(query:String){
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO){
        val BudgetFilter=dao.searchBudget(query)
       adapter.setadapter(BudgetFilter)
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
                    } } }
            .setNeutralButton("Cancel"){dialog,_-> dialog.dismiss() }
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