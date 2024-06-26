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
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Guest
import com.example.eventmatics.SwipeGesture.GuestSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GuestDataHolderActivity : AppCompatActivity(),GuestApdater.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    lateinit var guestAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter: GuestApdater
    private lateinit var Data_Not_found: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var GuestList:MutableList<Guest> = mutableListOf()
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
        if(recyclerView.adapter?.itemCount==0){
            Data_Not_found.visibility= View.VISIBLE
        }else{
            Data_Not_found.visibility= View.GONE
        }
        guestAdd.setOnClickListener {
            Intent(this,GuestDetails::class.java).also { startActivity(it) }
        }
        bottomnav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sort -> {
                    showSortOptions()
                    true
                }
                R.id.Filter ->{
                    showFilterOption()
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
                 GuestList=db.getAllGuests()
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
        GuestList=db.getAllGuests()
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
                            adapter.notifyItemRemoved(position)

                            MaterialAlertDialogBuilder(this@GuestDataHolderActivity)
                                .setTitle("Delete Item")
                                .setMessage("Do you want to delete this item?")
                                .setPositiveButton("Delete"){dialog,_->
                                    db.deleteGuest(deleteitem)
                                    recreate()
                                }
                                .setNegativeButton("Cancel"){dialog,_->
                                    dialog.dismiss()
                                    recreate()
                                }
                                .show()
                        }
                    ItemTouchHelper.RIGHT -> {
                        val guest = GuestList[position]
                        val currentStatus = guest.isInvitationSent
                        val newStatus = if (currentStatus == "Invitation Sent") "Not Sent" else "Invitation Sent"

                        when (newStatus) {
                            "Not Sent" -> MaterialAlertDialogBuilder(this@GuestDataHolderActivity)
                                .setTitle("Guest Invitation Status")
                                .setMessage("Is Guest Invitation Sent?")
                                .setPositiveButton("Yes") { dialog, _ ->
                                    db.updateGuestInvitation(guest.id, newStatus)
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
                                    db.updateGuestInvitation(guest.id, newStatus)
                                    recreate()
                                }
                                .setNeutralButton("No") { dialog, _ ->
                                    dialog.dismiss()
                                    recreate()
                                }
                                .show()
                        }
                    }

                } } }
        val itemtouch= ItemTouchHelper(swipe)
        itemtouch.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        showData()
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
                    }

                }
                dialog.dismiss()
            }
        .setNeutralButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        .show()
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

            .setNeutralButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .show()
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