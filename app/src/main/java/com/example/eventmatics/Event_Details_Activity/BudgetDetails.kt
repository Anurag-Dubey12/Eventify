package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.Adapter.PaymentActivity
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo
import com.example.eventmatics.data_class.SpinnerItem
import com.example.eventmatics.fragments.BudgetFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BudgetDetails : AppCompatActivity(), BudgetFragment.UserDataListener,BudgetFragment.PendingAmountListener,BudgetFragment.PaidAmountListener {

   lateinit var nameEditText:EditText
    lateinit var  balanceET: TextView
    lateinit var remainingET: TextView
    lateinit var EstimatedEt: EditText
    lateinit var NoteET: EditText
    var fragmentManager:FragmentManager=supportFragmentManager
    lateinit var PaymentAdd:ImageView
    lateinit var paidET: TextView
    lateinit var categoryButton: AppCompatButton
    lateinit var recyclerView :RecyclerView
    lateinit var  adapter:PaymentActivity
    lateinit var paymentList: MutableList<Paymentinfo>

    val spinnerItems = listOf(
        SpinnerItem(R.drawable.home, "Accessories"),
        SpinnerItem(R.drawable.home, "Accommodation"),
        SpinnerItem(R.drawable.home, "Attire & accessories"),
        SpinnerItem(R.drawable.home, "Ceremony"),
        SpinnerItem(R.drawable.home, "Flower & Decor"),
        SpinnerItem(R.drawable.home, "Health & Beauty"),
        SpinnerItem(R.drawable.home, "Jewelry"),
        SpinnerItem(R.drawable.home, "Miscellaneous"),
        SpinnerItem(R.drawable.home, "Music & Show"),
        SpinnerItem(R.drawable.home, "Photo & Video"),
        SpinnerItem(R.drawable.home, "Reception"),
        SpinnerItem(R.drawable.home, "Transportation")
    )
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
        balanceET = findViewById(R.id.Balancetv)
        remainingET = findViewById(R.id.RemainingET)
        paidET= findViewById(R.id.PaidET)
        recyclerView = findViewById(R.id.paymenttrans)
        PaymentAdd=findViewById(R.id.PaymentAdd)
        categoryButton = findViewById(R.id.category_button)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseApp.initializeApp(this)



        categoryButton.setOnClickListener {
            showCategoryPopup()
        }
        PaymentAdd.setOnClickListener {
            addpaymenttran()
        }

        //recyclerview code
// Initialize an empty mutable list to store payment information
        paymentList= mutableListOf()
        // Initialize the adapter with the empty payment list
        adapter=PaymentActivity(paymentList)
        // Set the adapter for the RecyclerView
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        estimatedAmountcalculate()


    }


    private fun estimatedAmountcalculate() {



        val initialres=R.drawable.drop_arrow
        balanceET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,initialres,0)
        balanceET.setOnClickListener {
            if(remainingET.visibility==View.GONE){
                val newdrawable=R.drawable.up_arrow
                balanceET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,newdrawable,0)

                remainingET.visibility=View.VISIBLE
                paidET.visibility=View.VISIBLE
            }
            else{
                val newdrawable=R.drawable.drop_arrow
                balanceET.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,newdrawable,0)
                remainingET.visibility=View.GONE
                paidET.visibility=View.GONE
            }
        }
        EstimatedEt.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(edit: Editable?) {
                val balancepretext=balanceET.text
                balanceET.text="Balance:+${EstimatedEt.text}"
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    private fun showCategoryPopup() {
        val spinnerAdapter = CategoryAdapter(this, spinnerItems)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Select Category")
            .setAdapter(spinnerAdapter) { _, position ->
                val selectedItem = spinnerItems[position]
               categoryButton.text=selectedItem.text
                categoryButton.setCompoundDrawablesWithIntrinsicBounds( 0, 0,selectedItem.imageres, 0)

            }
            .setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    // Function to add a payment transaction
    private fun addpaymenttran() {
        val bottomsheet=BudgetFragment()
        // Set the UserDataListener to the BudgetFragment
        bottomsheet.setUserDataListener(this)
        bottomsheet.pendingamountlistner = this
        bottomsheet.paidamountlistner = this
        bottomsheet.show(fragmentManager,"bottomsheet")
    }
// Implementation of the UserDataListener interface
    override fun onUserDataEntered(userData: Paymentinfo) {
    // Add the entered payment data to the paymentList
    paymentList.add(userData)
        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
               onBackPressed()
                true
            }
          R.id.Check->{
              AddValueToDataBase()
              true
          }
            else->super.onOptionsItemSelected(item)
        }

    }

    private fun AddValueToDataBase() {
        val name=nameEditText.text.toString()
        val amount=EstimatedEt.text.toString()
        val note = NoteET.text.toString()
//        val balance = balanceET.text.toString().toFloat()
//        val remaining = remainingET.text.toString().toFloat()
//        val paid = paidET.text.toString().toFloat()

        if (name.isEmpty()  ){
            nameEditText.error = "Please enter a name"
            return
        }
        if (amount.isEmpty()){
            EstimatedEt.error = "Please enter an amount"
            return
        }
        val db=FirebaseDatabase.getInstance().reference
        val newdata=db.child("data").push()

        val data=HashMap<String,Any>()
        data["name"]=name
        data["amount"]=amount
        data["note"]=note

        newdata.setValue(data).addOnSuccessListener {
            Toast.makeText(this@BudgetDetails,"Added Details",Toast.LENGTH_SHORT).show()

                }.addOnFailureListener {
            Toast.makeText(this@BudgetDetails,"Something went wrong",Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onPendingAmountSelected(amount: Float) {
        val displayText = "Pending: $amount"
        remainingET.text = displayText
        val total_amount=EstimatedEt.text.toString().toFloat()
        val final_amount=total_amount-amount
        val balanceamount="Balance:$final_amount"
        balanceET.text=balanceamount
    }

    override fun onPaidAmountSelected(amount: Float) {
        val displayText = "Paid: $amount"
        paidET.text = displayText
        val balanceString = balanceET.text.toString()
        val balanceNumericString = balanceString.substringAfterLast(":")
        val totalAmount = balanceNumericString.toFloat()
        val finalAmount = totalAmount - amount
        val balanceAmount = "Balance: $finalAmount"
        balanceET.text = balanceAmount
    }


}