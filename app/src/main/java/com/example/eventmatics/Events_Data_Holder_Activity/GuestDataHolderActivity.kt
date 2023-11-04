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
import com.example.eventmatics.Adapter.GuestApdater
import com.example.eventmatics.Event_Details_Activity.GuestDetails
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.GuestEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Guest
import com.example.eventmatics.SwipeGesture.GuestSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GuestDataHolderActivity : AppCompatActivity(),GuestApdater.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    lateinit var guestAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter: GuestApdater
    private lateinit var Data_Not_found: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var GuestList:MutableList<GuestEntity> = mutableListOf()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_data_holder)
        recyclerView = findViewById(R.id.guestDatarec)
        Data_Not_found = findViewById(R.id.data_not_found)
        guestAdd=findViewById(R.id.fab)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        bottomnav=findViewById(R.id.bottomNavigationView)
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)
        bottomnav.background=null
        //Action Bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        GlobalScope.launch(Dispatchers.Main){
            RoomDatabaseManager.initialize(applicationContext)
        }
        Data_Not_found.visibility=if(recyclerView.adapter?.itemCount==0) View.VISIBLE else View.GONE
        guestAdd.setOnClickListener { Intent(this,GuestDetails::class.java).also { startActivity(it) } }
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> { showSortOptions()
                    true
                }
                R.id.Filter ->{ showFilterOption()
                    true
                }
                else -> false
            } }
        showGuestData()
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                showGuestData()
                swipeRefreshLayout.isRefreshing=false
            },1)
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.Coral, R.color.Fuchsia, R.color.Indigo)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.Lemon_Chiffon)
        swipeRefreshLayout.setProgressViewOffset(false, 0, 150)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Check if the first visible item index is at the top
                val isAtTop = recyclerView.canScrollVertically(-1)
                swipeRefreshLayout.isEnabled =
                    !isAtTop // Enable/disable the SwipeRefreshLayout based on scroll position
            } }) }

    fun showGuestData(){
        GlobalScope.launch(Dispatchers.IO) {
            val dao = RoomDatabaseManager.getEventsDao(applicationContext)
            val guestList = dao.getAllGuests()

            runOnUiThread {
                if (guestList != null) {
                    adapter = GuestApdater(this@GuestDataHolderActivity, guestList, this@GuestDataHolderActivity)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(this@GuestDataHolderActivity)
                    adapter.notifyDataSetChanged()
                }
            }

            val swipe = object : GuestSwipeToDelete(this@GuestDataHolderActivity) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            if (position != RecyclerView.NO_POSITION) {
                                val deleteItem = guestList[position]
                                adapter.notifyItemRemoved(position)

                                MaterialAlertDialogBuilder(this@GuestDataHolderActivity)
                                    .setTitle("Delete Item")
                                    .setMessage("Do you want to delete this item?")
                                    .setPositiveButton("Delete") { dialog, _ ->
                                        GlobalScope.launch(Dispatchers.IO){
                                        dao.deleteGuest(deleteItem)
                                        }
                                        recreate()
                                    }
                                    .setNegativeButton("Cancel") { dialog, _ ->
                                        dialog.dismiss()
                                        recreate()
                                    }
                                    .show()
                            }
                        }
                        ItemTouchHelper.RIGHT -> {
                            val guest = guestList[position]
                            val currentStatus = guest.isInvitationSent
                            val newStatus = if (currentStatus == "Invitation Sent") "Not Sent" else "Invitation Sent"

                            when (newStatus) {
                                "Not Sent" -> MaterialAlertDialogBuilder(this@GuestDataHolderActivity)
                                    .setTitle("Guest Invitation Status")
                                    .setMessage("Is Guest Invitation Sent?")
                                    .setPositiveButton("Yes") { dialog, _ ->
                                        GlobalScope.launch(Dispatchers.IO) {
                                            dao.updateGuestInvitation(guest.id, newStatus)
                                        }
                                        recreate()
                                    }
                                    .setNeutralButton("No") { dialog, _ ->
                                        dialog.dismiss()
                                        recreate()
                                    }
                                    .show()
                                "Invitation Sent" -> MaterialAlertDialogBuilder(this@GuestDataHolderActivity)
                                    .setTitle("Guest Invitation Status")
                                    .setMessage("Is Guest Invitation Sent?")
                                    .setPositiveButton("Yes") { dialog, _ ->
                                        GlobalScope.launch(Dispatchers.IO) {
                                            dao.updateGuestInvitation(guest.id, newStatus)
                                        }
                                        recreate()
                                    }
                                    .setNeutralButton("No") { dialog, _ ->
                                        dialog.dismiss()
                                        recreate()
                                    }
                                    .show()
                            }
                        }
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipe)
            runOnUiThread { itemTouchHelper.attachToRecyclerView(recyclerView) }
        }
    }
    override fun onResume() {
        super.onResume()
        showGuestData()
    }
    private fun showSortOptions() {
        val SortValue= arrayOf("Alphabetic(A-Z)","No Of Family Member")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort Data")
            .setSingleChoiceItems(SortValue,-1){dialog,which->
                when(SortValue[which]){
                    "Alphabetic(A-Z)" ->{
                        GuestList.sortBy { it.name }
                        adapter.notifyDataSetChanged()
                    }
                    "No Of Family Member" ->{
                        GuestList.sortBy { it.totalFamilyMembers }
                        adapter.notifyDataSetChanged()
                    } }
                dialog.dismiss() }
        .setNeutralButton("Cancel"){dialog,_-> dialog.dismiss() }.show()
    }
    private fun showFilterOption() {
        val Filter= arrayOf("Invitation Sent","Not Sent")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort Data")
            .setSingleChoiceItems(Filter,-1){dialog,which->
                val InvitationselectedFilter=Filter[which]
                val InvitationFilterList=GuestList.filter {
                    it.isInvitationSent==InvitationselectedFilter
                }
               adapter.setdata(InvitationFilterList.toMutableList())
                dialog.dismiss()
                }
            .setNeutralButton("Cancel"){dialog,_-> dialog.dismiss() }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.holder,menu)
        val searchitem=menu?.findItem(R.id.action_search)
        val searchview=searchitem?.actionView as androidx.appcompat.widget.SearchView
        searchview.queryHint="Search"
        searchview.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean { SearchGuest(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean { SearchGuest(newText)
                return true
            } })
        return true
    }
    fun SearchGuest(query:String){
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        GlobalScope.launch(Dispatchers.IO){
        val GuestList=dao.searchGuests(query)
        adapter.setdata(GuestList)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{ onBackPressed()
                true
            }
            else->super.onOptionsItemSelected(item) } }
    override fun onItemClik(guestlist: GuestEntity) {
        Intent(this,GuestDetails::class.java).also {
            it.putExtra("selected_list",guestlist)
            startActivity(it)
        } } }