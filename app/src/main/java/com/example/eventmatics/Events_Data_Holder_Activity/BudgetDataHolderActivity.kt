package com.example.eventmatics.Events_Data_Holder_Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.BudgetDataHolderAdapter
import com.example.eventmatics.Event_Details_Activity.BudgetDetails
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class BudgetDataHolderActivity : AppCompatActivity(),BudgetDataHolderAdapter.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    lateinit var budgetAdd: FloatingActionButton
    lateinit var bottomnav: BottomNavigationView
    lateinit var adapter:BudgetDataHolderAdapter
    lateinit var budgetlist:MutableList<Budget>
//    lateinit var paymentlist:Paymentinfo
    private var filteredList: MutableList<Budget> = mutableListOf()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onItemClick(budget: Budget) {
        Intent(this, BudgetDetails::class.java).apply {
            putExtra("selected_item", budget)
            startActivity(this)
        }
    }
//    override fun onItemClick(payment: Paymentinfo) {
//        val intent = Intent().apply {
//            putExtra("Selected_Item", payment)
//        }
//        startActivityForResult(intent, 230)
//    }

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
        recyclerView.setOnLongClickListener {

            true
        }
        if(recyclerView.adapter?.itemCount==0){

        }
        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                val databasename=getSharedPreference(this,"databasename").toString()
                val databasehelper = LocalDatabase(this, databasename)
                val BudgetList = databasehelper.getAllBudgets()
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
    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
    fun removeSharedPreference(context: Context, key: String) {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove(key)
        editor.apply()
    }

    private fun showbudgetlist() {
        val databasename=getSharedPreference(this,"databasename").toString()
        val databasehelper = LocalDatabase(this, databasename)
        val BudgetList = databasehelper.getAllBudgets()

        if(BudgetList!=null){
            adapter = BudgetDataHolderAdapter(this,BudgetList,this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        val swipe=object:BudgetSwipeToDelete(this){
            var deletedItem: Budget? = null
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val newPaidStatus:String
                var previousPaidStatus:String
                var NewBalance:Float
                val bal=0;
                when(direction){
                    ItemTouchHelper.LEFT->{
                if(position!=RecyclerView.NO_POSITION){
                    deletedItem=BudgetList[position]
                    databasehelper.deleteBudget(deletedItem!!)
                    adapter.notifyItemRemoved(position)

                    val snackbar=Snackbar.make(this@BudgetDataHolderActivity.recyclerView,"Budget Item Deleted",Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO") {
                        // Undo the delete operation
                        if (deletedItem != null) {
                            databasehelper.createBudget(deletedItem!!)
                            BudgetList.add(position, deletedItem!!)
                            adapter.notifyItemInserted(position)
                            adapter.notifyDataSetChanged()

                        }
                    }
                        .addCallback(object:BaseTransientBottomBar.BaseCallback<Snackbar>(){
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)
                                if (event!= DISMISS_EVENT_ACTION){
                                    deletedItem=null
                                }
//                                recreate()
                            }
//                            override fun onShown(transientBottomBar: Snackbar?) {
//                                transientBottomBar?.setAction("UNDO"){
//                                    BudgetList.add(position,deleteitem)
//                                    adapter.notifyItemInserted(position)
//                                }
//
//                                super.onShown(transientBottomBar)
//                            }
                        }).apply {
                            animationMode = Snackbar.ANIMATION_MODE_FADE
                        }
                    snackbar.setActionTextColor(
                        ContextCompat.getColor(
                            this@BudgetDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                        )
                    )
                    snackbar.show()

                }
                    }
                    ItemTouchHelper.RIGHT->{
                        if(position!=RecyclerView.NO_POSITION){
                            val budget = BudgetList[position]

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
                            // Update the 'Paid' status in the database
                            val rowsAffected = databasehelper.updateBudgetPaid(budget.id, newPaidStatus,"$NewBalance")
                            if (rowsAffected > 0) {
                                val snackbar=Snackbar.make(this@BudgetDataHolderActivity.recyclerView,"Budget Data Updated",Snackbar.LENGTH_LONG)
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
//
//                                            val previousPaidStatus = if (newPaidStatus == "Paid") "Not Paid" else "Paid"
                                            if (newPaidStatus == "Paid"){
                                                previousPaidStatus="Not Paid"
                                                NewBalance=budget.estimatedAmount.toFloat()
                                            } else {
                                                previousPaidStatus="Paid"
                                                NewBalance=bal.toFloat()
                                            }
                                            databasehelper.updateBudgetPaid(budget.id, previousPaidStatus,"$NewBalance")
                                        recreate()
                                        }
                                        super.onShown(transientBottomBar)
                                    }
                                }).apply {
                                    animationMode=Snackbar.ANIMATION_MODE_FADE
                                }
                            snackbar.setActionTextColor(
                                ContextCompat.getColor(
                                    this@BudgetDataHolderActivity, androidx.browser.R.color.browser_actions_bg_grey
                                )
                            )
                            snackbar.show()

                        }
                    }
                        //Payment Gateway code
//                        val budget = BudgetList[position]
//                        val getdata=budget.estimatedAmount
//                        val amount=getdata.toFloat()
//                        var parseamount=Math.round(amount!! *100)
//                        val checkout = Checkout()
//                        val name=budget.name
//                        // set your id as below
//                        checkout.setKeyID("rzp_test_nIeiFSu0nyqFRK")
//
//                        // set image
//                        checkout.setImage(R.drawable.event_management)
//                        Checkout.preload(applicationContext)
//                        // initialize json object
//                        val `object` = JSONObject()
//                        try {
//                            // to put name
//                            `object`.put("name", "$name")
//
//                            // put description
//                            `object`.put("description", "Budget Payment")
//
//                            // to set theme color
//                            `object`.put("theme.color", "#FF81D4FA")
//
////                            `object`.put("order_id", "order_DBJOWzybf0sJbb");
//                            // put the currency
//                            `object`.put("currency", "INR")
//
//                            // put amount
//                            `object`.put("amount", parseamount)
//                            val retryObj = JSONObject()
//                            retryObj.put("enabled", true);
//                            retryObj.put("max_count", 4);
//                            `object`.put("retry", retryObj);
//
//                            val prefill = JSONObject()
//                            prefill.put("email","ad210689@gmail.com")
//                            prefill.put("contact","9004040592")
//                            `object`.put("prefill",prefill)
                            // put mobile number
//                            `object`.put("prefill.contact", "")
//                            `object`.put("send_sms_hash",true);
//
//                            `object`.put("receipt","order_rcptid_11")

                            // open razorpay to checkout activity
//                            Checkout.clearUserData(this@BudgetDataHolderActivity)
//                checkout.open(this@BudgetDataHolderActivity, `object`)
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//                        var budgetid=budget.id
//                        val BudgetName=budget.name
//                        onPaymentSuccess("For $BudgetName",budgetid)
//
//                         onPaymentError("Something Went Wrong",budgetid)

                }
            }
            }
        }
        val itemTouch=ItemTouchHelper(swipe)
        itemTouch.attachToRecyclerView(recyclerView)
    }
//    fun onPaymentSuccess(s: String, budgetId: Long) {
//        val updated = updateBudgetAndUI(budgetId, "Paid")
//        if (updated) {
//            Toast.makeText(this@BudgetDataHolderActivity, "Payment is successful : $s", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this@BudgetDataHolderActivity, "Payment was successful but failed to update status.", Toast.LENGTH_SHORT).show()
//        }
//    }

//    fun onPaymentError(s: String, budgetId: Long) {
//        val update=updateBudgetAndUI(budgetId,"Not Paid")
//        if(update){
//            Toast.makeText(this@BudgetDataHolderActivity, "Payment is not successful : $s", Toast.LENGTH_SHORT).show()
//
//        }
//        else{
//            Toast.makeText(this@BudgetDataHolderActivity, "Payment Failed due to error : $s", Toast.LENGTH_SHORT).show()
//        }
//
//    }
    private fun updateBudgetAndUI(budgetId: Long, newStatus: String): Boolean {
        val databasename = getSharedPreference(this, "databasename").toString()
        val databasehelper = LocalDatabase(this, databasename)
        val updatedRows = databasehelper.updateBudgetPaid(budgetId, newStatus, "0.0")
        val BudgetList = databasehelper.getAllBudgets()

        if (updatedRows > 0) {
            val positionToUpdate = BudgetList.indexOfFirst { it.id == budgetId }
            if (positionToUpdate != -1) {
                BudgetList[positionToUpdate].paid = newStatus
                adapter.notifyItemChanged(positionToUpdate)
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        val databasename=getSharedPreference(this,"databasename").toString()
        val databasehelper = LocalDatabase(this, databasename)
        val BudgetList = databasehelper.getAllBudgets()
        if(BudgetList!=null){
            adapter = BudgetDataHolderAdapter(this,BudgetList,this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
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
        val databasename=getSharedPreference(this,"databasename").toString()
        val db = LocalDatabase(this, databasename)
        val BudgetFilter=db.SearchBudget(query)
       adapter.setadapter(BudgetFilter)

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
            filteredList.addAll(budgetlist.filter { it.remaining == "Pending" })
            adapter.updateList(filteredList)
            dialog.dismiss()
        }

        paidOnly.setOnClickListener {
            filteredList.clear()
            filteredList.addAll(budgetlist.filter { it.paid== "Paid" })
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