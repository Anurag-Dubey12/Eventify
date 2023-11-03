package com.example.eventmatics.Events_Data_Holder_Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.VendorDataHolderClass
import com.example.eventmatics.Event_Details_Activity.VendorDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Vendor
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VendorDataHolderActivity : AppCompatActivity(),VendorDataHolderClass.onItemClickListener {
    private lateinit var recyclerView: RecyclerView
    lateinit var vendorAdd: FloatingActionButton
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:VendorDataHolderClass
    private lateinit var Data_Not_found: ImageView
    private var vendorlist:MutableList<Vendor> = mutableListOf()
    override fun onItemclick(vendor: Vendor) {
        Intent(this,VendorDetails::class.java).also {
            it.putExtra("Selected_Item",vendor)
            startActivity(it)
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_data_holder)
        recyclerView = findViewById(R.id.vendorDatarec)
        Data_Not_found = findViewById(R.id.data_not_found)
        vendorAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        Data_Not_found.visibility=if(recyclerView.adapter?.itemCount==0) View.VISIBLE else View.GONE
        vendorAdd.setOnClickListener { Intent(this, VendorDetails::class.java).also { startActivity(it) } }

        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    showSortOptions()
                    true
                }
                R.id.Filter -> {
                    showfilteroption()
                    true
                }
                else -> false
            }
        }
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val db=DatabaseManager.getDatabase(this)
                vendorlist=db.getAllVendors()
                if(vendorlist!=null){
                    adapter=VendorDataHolderClass(this,vendorlist,this)
                    recyclerView.adapter=adapter
                    recyclerView.layoutManager=LinearLayoutManager(this)
                }
                    swipeRefreshLayout.isRefreshing=false
            },1)
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.Coral, R.color.Fuchsia, R.color.Indigo)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Lemon_Chiffon)
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150)
        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            val isAtTop=recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled=!isAtTop
            } })
        showData()
    }
    private fun showData() {
        val db=DatabaseManager.getDatabase(this)
        vendorlist=db.getAllVendors()
        if(vendorlist!=null){
            adapter=VendorDataHolderClass(this,vendorlist,this)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this)
        }
        val swipe=object:BudgetSwipeToDelete(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                when(direction){

                ItemTouchHelper.LEFT->{
                if(position!=RecyclerView.NO_POSITION){
                    val deleteditem=vendorlist[position]

                    adapter.notifyItemRemoved(position)
                    MaterialAlertDialogBuilder(this@VendorDataHolderActivity)
                        .setTitle("Delete Item")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Delete"){dialog,_-> db.deleteVendor(deleteditem)
                            recreate()
                        }
                        .setNegativeButton("Cancel"){dialog,_-> dialog.dismiss()
                            recreate()
                        }.show()
                } }}}
        }
        val itemtouch=ItemTouchHelper(swipe)
        itemtouch.attachToRecyclerView(recyclerView)
    }
    override fun onResume() {
        super.onResume()
        val db=DatabaseManager.getDatabase(this)
        vendorlist=db.getAllVendors()
        if(vendorlist!=null){
            adapter=VendorDataHolderClass(this,vendorlist,this)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.holder,menu)
        val searchitem=menu?.findItem(R.id.action_search)
        val searchview=searchitem?.actionView as androidx.appcompat.widget.SearchView
        searchview.queryHint="Search"

        searchview.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                SearchVendor(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                SearchVendor(newText)
                return true
            } })
        return true
    }
    fun SearchVendor(query:String){
        val db=DatabaseManager.getDatabase(this)
        val VendorList=db.SearchVendor(query)
        adapter.setdata(VendorList)
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

    private fun showfilteroption() {
        val FilterValue= arrayOf("Paid","Not Paid")
        MaterialAlertDialogBuilder(this)
            .setTitle("Filter Data")
            .setSingleChoiceItems(FilterValue,-1){dialog,which->
                val SelectedFilter=FilterValue[which]
                val FilterList=vendorlist.filter {vendor->
                    vendor.paid==SelectedFilter
                }
                adapter.setdata(FilterList.toMutableList())
                dialog.dismiss()
            }
            .setNeutralButton("Cancel"){dialog,_-> dialog.dismiss() }.show()
    }

    private fun showSortOptions() {
        val SortValue= arrayOf("Alphabetic(A-Z)","Amount")

        val DilogShow= MaterialAlertDialogBuilder(this)
            .setTitle("Sort Data")
            .setSingleChoiceItems(SortValue,-1){dialog,which->
                when(SortValue[which]){
                    "Alphabetic(A-Z)"->{
                        vendorlist.sortBy { it.name }
                        adapter.notifyDataSetChanged()
                    }
                    "Amount"->{
                        vendorlist.sortBy { it.estimatedAmount }
                        adapter.notifyDataSetChanged()
                    } }
                dialog.dismiss() }
            .setNeutralButton("Cancel"){dialog,_-> dialog.dismiss() }
        DilogShow.show()
    }
}