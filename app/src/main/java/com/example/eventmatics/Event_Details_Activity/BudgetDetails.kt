package com.example.eventmatics.Event_Details_Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.Adapter.PaymentActivityAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.data_class.Paymentinfo
import com.example.eventmatics.data_class.SpinnerItem
import com.example.eventmatics.fragments.BudgetFragment

class BudgetDetails : AppCompatActivity(),BudgetFragment.OnDataEnter{
    lateinit var nameEditText:EditText
    lateinit var  balanceET: TextView
    lateinit var  PaidET: TextView
    lateinit var  PaymentRecycler: RecyclerView
    lateinit var  Paymentadd: ImageView
    lateinit var  addapyment: LinearLayout
    lateinit var  adapter: PaymentActivityAdapter
    var paymentlist:MutableList<Paymentinfo> = mutableListOf()
    lateinit var EstimatedEt: EditText
    val fragmentManager = supportFragmentManager
    lateinit var NoteET: EditText
    lateinit var budgetFragment: BudgetFragment
    lateinit var PaymentBalance: LinearLayout
    lateinit var categoryselection: TextView

    val spinnerItems = listOf(
        SpinnerItem("Accessories"),
        SpinnerItem( "Accommodation"),
        SpinnerItem( "Attire & accessories"),
        SpinnerItem( "Ceremony"),
        SpinnerItem( "Flower & Decor"),
        SpinnerItem( "Health & Beauty"),
        SpinnerItem( "Jewelry"),
        SpinnerItem( "Miscellaneous"),
        SpinnerItem( "Music & Show"),
        SpinnerItem( "Photo & Video"),
        SpinnerItem( "Reception"),
        SpinnerItem( "Transportation")
    )
    override fun ondataenter(userdata: Paymentinfo) {
        paymentlist.add(userdata)
        adapter.notifyDataSetChanged()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_details)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Id's
        nameEditText = findViewById(R.id.NameET)
        EstimatedEt = findViewById(R.id.Estimated_Amount)
        NoteET = findViewById(R.id.NoteET)
        PaymentRecycler = findViewById(R.id.PaymentRecycler)
        Paymentadd = findViewById(R.id.paymentadd)
        balanceET = findViewById(R.id.Balancetv)
        PaymentBalance = findViewById(R.id.PaymentBalance)
        addapyment = findViewById(R.id.addapyment)
        categoryselection = findViewById(R.id.categoryselection)

        categoryselection.setOnClickListener {
            showCategoryPopup()
        }

        val selected_item:Budget?=intent.getParcelableExtra("selected_item")
//        val selected_item_payment:Paymentinfo?=intent.getParcelableExtra("Selected_Item")

        if (selected_item!=null){
            PaymentBalance.visibility=View.VISIBLE
            addapyment.visibility=View.VISIBLE
            PaymentRecycler.visibility=View.VISIBLE
            nameEditText.setText(selected_item.name)
            categoryselection.setText(selected_item.category)
            NoteET.setText(selected_item.note)
            balanceET.setText(selected_item.balance)
            EstimatedEt.setText(selected_item.estimatedAmount)

            val id = selected_item?.id!!.toInt()

            val databasename = getSharedPreference(this@BudgetDetails, "databasename").toString()
            val databasehelper = LocalDatabase(this@BudgetDetails, databasename)
            val paymentData = databasehelper.getPaymentsForBudget(id)
            for (payment in paymentData) {
                Log.d("PaymentData", "ID: ${payment.id}, Name: ${payment.name}, Amount: ${payment.amount}")
            }
            adapter=PaymentActivityAdapter(this,paymentData)
            PaymentRecycler.adapter = adapter
            PaymentRecycler.layoutManager = LinearLayoutManager(this)
            paymentlist.clear()
            paymentlist.addAll(paymentData)
            adapter.notifyDataSetChanged()
        }

         budgetFragment= BudgetFragment(this, supportFragmentManager, selected_item?.id)
        budgetFragment.setUserDataListner(this)
        adapter = PaymentActivityAdapter(this@BudgetDetails, paymentlist)

        Paymentadd.setOnClickListener {
            showpaymentsheet()
        }
    }

    private fun showpaymentsheet() {
        budgetFragment.show(supportFragmentManager, "budgetFragmentTag")

    }

    private fun showCategoryPopup() {
        val spinnerAdapter = CategoryAdapter(this, spinnerItems)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Select Category")
            .setAdapter(spinnerAdapter) { _, position ->
                val selectedItem = spinnerItems[position]
                categoryselection.text=selectedItem.text
            }
            .setNegativeButton("Cancel", null)
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
               onBackPressed()
                true
            }
          R.id.Check->{
              val selected_item:Budget?=intent.getParcelableExtra("selected_item")
              if (selected_item!=null){

                  UpdateDatabase(selected_item.id,paymentlist)
              }
              else{
              AddValueToDataBase()
              }
              true
          }
            else->super.onOptionsItemSelected(item)
        }
    }
    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    private fun UpdateDatabase(id: Long, paymentList: List<Paymentinfo>) {
        val name = nameEditText.text.toString()
        val totalamt = EstimatedEt.text.toString().toFloat()
        val Totalamount = totalamt.toString()
        val note = NoteET.text.toString()
        val category = categoryselection.text.toString()
        val balance = balanceET.text.toString()

        if (name.isEmpty()) {
            nameEditText.error = "Please enter a name"
            return
        }
        if (totalamt == 0f) {
            EstimatedEt.error = "Please enter an amount"
            return
        }

        val status: String
        val databasename = getSharedPreference(this, "databasename").toString()
        val db = LocalDatabase(this, databasename)
        val isBudgetPaid = db.isBudgetPaid(id)
        if (isBudgetPaid) {
            status = "Paid"
        } else {
            status = "Not Paid"
        }

        val budget = Budget(id, name, category, note, Totalamount, balance, "", status)
        db.updateBudget(budget)

        for (payment in paymentList) {
            if (payment.budgetid == id) {
                db.createPayment(payment)
            }
        }

        Toast.makeText(this, "Budget Updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

//    private fun UpdateDatabase(id: Long) {
//        val name = nameEditText.text.toString()
//        val totalamt = EstimatedEt.text.toString().toFloat()
//        val Totalamount=totalamt.toString()
//        val note=NoteET.text.toString()
//        val category=categoryselection.text.toString()
//        val balance=balanceET.text.toString()
//
//        if (name.isEmpty()) {
//            nameEditText.error = "Please enter a name"
//            return }
//        if (totalamt==0f) {
//            EstimatedEt.error = "Please enter an amount"
//            return
//        }
//        val status:String
//        val databasename=getSharedPreference(this,"databasename").toString()
//        val db=LocalDatabase(this,databasename)
//        val isBudgetPaid=db.isBudgetPaid(id)
//        if(isBudgetPaid){
//            status="Paid"
//        }
//        else{
//            status="Not Paid"
//        }
//        val budget=Budget(id,name,category,note,Totalamount,balance,"","$status")
//        db.updateBudget(budget)
//        Toast.makeText(this, "Budget Updated successfully", Toast.LENGTH_SHORT).show()
//        finish()
//    }

    private fun AddValueToDataBase() {
        val name = nameEditText.text.toString()
        val totalamt = EstimatedEt.text.toString().toFloat()
        val Totalamount=totalamt.toString()
        val note=NoteET.text.toString()
        val category=categoryselection.text.toString()
        val balance=balanceET.text.toString()

        if (name.isEmpty()) {
            nameEditText.error = "Please enter a name"
            return }
        if (totalamt==0f) {
            EstimatedEt.error = "Please enter an amount"
            return
        }

        val id=0
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val budget=Budget(id.toLong(),name,category,note,Totalamount,balance,"","Not Paid")
        db.createBudget(budget)
        Toast.makeText(this, "Budget Added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


}
