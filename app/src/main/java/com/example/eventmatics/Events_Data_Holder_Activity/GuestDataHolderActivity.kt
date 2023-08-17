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
import com.example.eventmatics.Adapter.GuestApdater
import com.example.eventmatics.Event_Details_Activity.GuestDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Guest
import com.example.eventmatics.SwipeGesture.GuestSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class GuestDataHolderActivity : AppCompatActivity(),GuestApdater.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    lateinit var guestAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter: GuestApdater
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_data_holder)
        recyclerView = findViewById(R.id.guestDatarec)
        guestAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
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
        showData()
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databasename=getSharedPreference(this,"databasename").toString()
                val db=LocalDatabase(this,databasename)
                val GuestList=db.getAllGuests()
                val tot=db.getTotalInvitationsSent()
                val nottot=db.getTotalInvitationsNotSent()
                if(GuestList!=null){
                    val adapter=GuestApdater(this,GuestList,this)
                    recyclerView.adapter=adapter
                    recyclerView.layoutManager=LinearLayoutManager(this)
                    adapter.notifyDataSetChanged()
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

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Check if the first visible item index is at the top
                val isAtTop = recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled =
                    !isAtTop // Enable/disable the SwipeRefreshLayout based on scroll position
            }
        })
    }
    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
    fun showData(){
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val GuestList=db.getAllGuests()
        if(GuestList!=null){
            val adapter=GuestApdater(this,GuestList,this)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val GuestList=db.getAllGuests()
        if(GuestList!=null){
            adapter=GuestApdater(this,GuestList,this)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this)
            adapter.notifyDataSetChanged()
        }
        val swipe=object : GuestSwipeToDelete(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.LEFT->
                if(position!=RecyclerView.NO_POSITION){
                    val deleteitem=GuestList[position]
                    db.deleteGuest(deleteitem)
                    adapter.notifyItemRemoved(position)

                    val snackbar= Snackbar.make(this@GuestDataHolderActivity.recyclerView,"Guest Data Removed",Snackbar.LENGTH_LONG)
                        .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                recreate()
                            }

                            override fun onShown(transientBottomBar: Snackbar?) {
                                transientBottomBar?.setAction("UNDO"){
                                    GuestList.add(position,deleteitem)
                                    adapter.notifyItemInserted(position)
                                }
                                super.onShown(transientBottomBar)
                            }
                        }).apply {
                            animationMode=Snackbar.ANIMATION_MODE_SLIDE
                        }
                    snackbar.setActionTextColor(
                        ContextCompat.getColor(
                            this@GuestDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                        )
                    )
                    snackbar.show()
                }
                    ItemTouchHelper.RIGHT->{
                        val Guest=GuestList[position]
                        val CurrentStatus=Guest.isInvitationSent
                        val New_Status=if(CurrentStatus=="Invitation Sent") "Not Sent" else "Invitation Sent"
                        val RowAffected=db.updateGuestInvitation(Guest.id,New_Status)
                        if(RowAffected>0){
                            val snackbar=Snackbar.make(this@GuestDataHolderActivity.recyclerView,"Guest Data Updated",Snackbar.LENGTH_LONG)
                                .addCallback(object:BaseTransientBottomBar.BaseCallback<Snackbar>(){
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        super.onDismissed(transientBottomBar, event)
                                        recreate()
                                    }
                                    override fun onShown(transientBottomBar: Snackbar?) {
                                        transientBottomBar?.setAction("UNDO"){
                                            val previousPaidStatus =if(New_Status=="Invitation Sent") "Not Sent" else "Invitation Sent"
                                            db.updateGuestInvitation(Guest.id,previousPaidStatus)
                                        }
                                        recreate()
                                        super.onShown(transientBottomBar)
                                    }
                                }).apply {
                                    animationMode=Snackbar.ANIMATION_MODE_FADE
                                }
                            snackbar.setActionTextColor(
                                ContextCompat.getColor(
                                    this@GuestDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                                )
                            )
                            snackbar.show()
                        }

                    }
            }

        }
        }
        val itemtouch= ItemTouchHelper(swipe)
        itemtouch.attachToRecyclerView(recyclerView)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.holder,menu)

        val searchitem=menu?.findItem(R.id.action_search)
        val searchview=searchitem?.actionView as androidx.appcompat.widget.SearchView
        searchview.queryHint="Search"

        searchview.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                SearchGuest(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                SearchGuest(newText)
                return true

            }
        })
        return true
    }
    fun SearchGuest(query:String){
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val GuestList=db.SearchGuest(query)

        adapter.setdata(GuestList)
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

    override fun onItemClik(guestlist: Guest) {
        Intent(this,GuestDetails::class.java).also {
            it.putExtra("selected_list",guestlist)
            startActivity(it)
        }
    }
}