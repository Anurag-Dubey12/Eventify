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
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
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
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
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
                val db = LocalDatabase(this, databasename)
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
    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
//    fun removeSharedPreference(context: Context, key: String) {
//        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        editor.remove(key)
//        editor.apply()
//    }
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val pdfReportItem = menu.findItem(R.id.pdfreport)
        val check = menu.findItem(R.id.Check)
        pdfReportItem.isVisible = !isRecyclerViewEmpty
        return true
    }

    private fun showbudgetlist() {
        val databasename=getSharedPreference(this,"databasename").toString()
        val db = LocalDatabase(this, databasename)
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

                          MaterialAlertDialogBuilder(this@BudgetDataHolderActivity)
                              .setTitle("Budget Payment")
                              .setMessage("What is the Status of the Budget Payment?")
                              .setPositiveButton("PAID"){dialog,_->
                                  db.updateBudgetPaid(budget.id, newPaidStatus,"$NewBalance")
                                  recreate()
                              }
                              .setNegativeButton("UNPAID"){dialog,_->
                                  db.updateBudgetPaid(budget.id, newPaidStatus,"$NewBalance")
                                  recreate()
                              }
                              .setNeutralButton("Cancel"){dialog,_->
                                  dialog.dismiss()
                                  recreate()
                              }
                              .show()
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
        val db = LocalDatabase(this, databasename)
        val updatedRows = db.updateBudgetPaid(budgetId, newStatus, "0.0")
        val BudgetList = db.getAllBudgets()

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
            R.id.pdfreport->{
                if(checkPermissions()){
                    GeneratePDF()
                }
                else{
                    requestPermission()
                }
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }

    private fun GeneratePDF() {
        val databasename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databasename)
        val BudgetList = db.getAllBudgets()
        val Event = db.getEventData(1)
        val name=Event?.name

        val pageWidth = 792
        val pageHeight = 1120
        bmp = BitmapFactory.decodeResource(resources, R.drawable.logo)
        scalebmp = Bitmap.createScaledBitmap(bmp, 190, 190, false)

        val pdfDocument = PdfDocument()

        val paint = Paint()
        val title = Paint()

        val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(
            pageWidth, pageHeight, 1
        ).create()
        val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        canvas.drawBitmap(scalebmp, 20F, 0F, paint)
        title.textSize = 30F
        title.color = ContextCompat.getColor(this, R.color.black)
        canvas.drawText("Budget Report", 209F, 50F, title)

        val startY = 200F
        val lineHeight = 50F

        // Define column names
        val columnNames = listOf("No", "Name", "Category", "Note", "Estimated")

        // Draw the header text and borders for header cells
        val borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = Color.BLACK
        borderPaint.strokeWidth = 2F

        val headerPaint = Paint()
        headerPaint.textSize = 20F
        headerPaint.color = Color.BLACK
        headerPaint.textAlign = Paint.Align.CENTER

        val headerX = pageWidth.toFloat() / 2
        val headerY = startY

        for ((columnIndex, columnName) in columnNames.withIndex()) {
            val cellWidth = pageWidth / columnNames.size
            val cellHeight = lineHeight

            val cellX = columnIndex * cellWidth
            val cellY = headerY - cellHeight / 2

            // Draw borders around header cells
            canvas.drawRect(
                cellX.toFloat(), cellY, (cellX + cellWidth).toFloat(),
                (cellY + cellHeight), borderPaint
            )

            // Draw header text
            canvas.drawText(columnName,
                (cellX + cellWidth / 2).toFloat(), cellY + cellHeight / 2, headerPaint)
        }

        val dataPaint = Paint()
        dataPaint.textSize = 12F
        dataPaint.color = Color.BLACK
        dataPaint.textAlign = Paint.Align.CENTER

        val cellWidth = pageWidth / columnNames.size
        val cellHeight = lineHeight

        for ((rowIndex, Budget) in BudgetList.withIndex()) {
            val xPosition = 0
            val yPosition = startY + (rowIndex + 1) * lineHeight

            val dataItems = listOf(
                Budget.id.toString(),
                Budget.name,
                Budget.category,
                Budget.note,
                Budget.estimatedAmount
            )

            for ((columnIndex, data) in dataItems.withIndex()) {
                val cellX = columnIndex * cellWidth
                val cellY = yPosition - cellHeight / 2

                // Draw borders around data cells
                canvas.drawRect(
                    cellX.toFloat(), cellY, (cellX + cellWidth).toFloat(),
                    (cellY + cellHeight), borderPaint
                )

                // Draw data text
                canvas.drawText(data,
                    (cellX + cellWidth / 2).toFloat(), cellY + cellHeight / 2, dataPaint)
            }
        }
        pdfDocument.finishPage(page)


        val pdfDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        if (!pdfDirectory.exists()) {
            pdfDirectory.mkdirs()
        }
        val EventNameDirectory=File(pdfDirectory,name)
        if (!EventNameDirectory.exists()) {
            EventNameDirectory.mkdirs()
        }
        val childDirectory = File(EventNameDirectory, "Budget")
        if (!childDirectory.exists()) {
            childDirectory.mkdirs()
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val pdfFileName = "BudgetReport_$timestamp.pdf"
        val pdfFilePath = File(childDirectory, pdfFileName)

        try {
            pdfFilePath.createNewFile()
            pdfDocument.writeTo(FileOutputStream(pdfFilePath))
            Toast.makeText(this, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "PDF file generated successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("PDF", "Failed to generate PDF file: ${e.message}")
        }

        pdfDocument.close()
    }

    fun checkPermissions():Boolean{
        var writeStoragePermission=ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        var readStroragePermission=ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission==PackageManager.PERMISSION_GRANTED &&
                readStroragePermission==PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==PERMISSION_CODE){
            if(grantResults.size > 0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    GeneratePDF()
                }
                else{
                    Log.d("PDF", "Permission Denied.")

                }
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