package com.example.eventmatics.Events_Data_Holder_Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.VendorDataHolderClass
import com.example.eventmatics.Event_Details_Activity.VendorDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Vendor
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class VendorDataHolderActivity : AppCompatActivity(),VendorDataHolderClass.onItemClickListener {
    private lateinit var recyclerView: RecyclerView
    lateinit var vendorAdd: FloatingActionButton
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:VendorDataHolderClass

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
        vendorAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        vendorAdd.setOnClickListener {
            Intent(this, VendorDetails::class.java).also { startActivity(it) }
        }

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
                val databasename=getSharedPreference(this,"databasename").toString()
                val db=LocalDatabase(this,databasename)
                val vendorlist=db.getAllVendors()
                if(vendorlist!=null){
                    adapter=VendorDataHolderClass(vendorlist,this)
                    recyclerView.adapter=adapter
                    recyclerView.layoutManager=LinearLayoutManager(this)
                }
                    swipeRefreshLayout.isRefreshing=false
            },1)
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.Coral,
            R.color.Fuchsia,
            R.color.Indigo
        )
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Lemon_Chiffon)
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150)
        recyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            val isAtTop=recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled=!isAtTop
            }
        })
        showData()
    }

    private fun showData() {
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val vendorlist=db.getAllVendors()
        if(vendorlist!=null){
            adapter=VendorDataHolderClass(vendorlist,this)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this)
        }
        val swipe=object:BudgetSwipeToDelete(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    val deleteditem=vendorlist[position]
                    db.deleteVendor(deleteditem)
                    adapter.notifyItemRemoved(position)
                    val snackbar=Snackbar.make(this@VendorDataHolderActivity.recyclerView,"Vendor Data Deleted",Snackbar.LENGTH_LONG)
                        .addCallback(object:BaseTransientBottomBar.BaseCallback<Snackbar>(){
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                recreate()
                            }

                            override fun onShown(transientBottomBar: Snackbar?) {
                                super.onShown(transientBottomBar)
                                transientBottomBar?.setAction("UNDO"){
                                    vendorlist.add(position,deleteditem)
                                    adapter.notifyItemInserted(position)
                                }
                            }
                        }).apply {
                            animationMode=Snackbar.ANIMATION_MODE_FADE
                        }
                    snackbar.setActionTextColor(
                        ContextCompat.getColor(
                            this@VendorDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                        )
                    )
                    snackbar.show()
                }
            }
        }
        val itemtouch=ItemTouchHelper(swipe)
        itemtouch.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val vendorlist=db.getAllVendors()
        if(vendorlist!=null){
            adapter=VendorDataHolderClass(vendorlist,this)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this)
        }
    }
    fun getSharedPreference(context: Context, key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
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
            }

        })
        return true
    }
    fun SearchVendor(query:String){
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
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
            dialog.dismiss()
        }

        nameDescending.setOnClickListener {
            dialog.dismiss()
        }

        amountAscending.setOnClickListener {
            dialog.dismiss()
        }

        amountDescending.setOnClickListener {
            dialog.dismiss()
        }
    }


}