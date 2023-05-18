package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.example.eventmatics.Adapter.BudgetCategoryAdapter
//import com.example.eventmatics.Adapter.BudgetCategoryAdapter
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.example.eventmatics.data_class.SpinnerItem
import com.google.android.material.button.MaterialButton

class BudgetDetails : AppCompatActivity(R.layout.activity_budget_details) {
    lateinit var  balanceET: TextView
    lateinit var remainingET: TextView
    lateinit var EstimatedEt: EditText
    lateinit var PaymentAdd:ImageView
    lateinit var paidET: TextView
    lateinit var spinner: Spinner
lateinit var categoryButton: AppCompatButton
    lateinit var categoryPopupWindow: PopupWindow

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

        PaymentAdd=findViewById(R.id.PaymentAdd)

        categoryButton = findViewById(R.id.category_button)
        categoryButton.setOnClickListener {
            showCategoryPopup()
        }
        PaymentAdd.setOnClickListener {
            addpaymenttran()
        }

        estimatedAmountcalculate()
    }

    private fun addpaymenttran() {

    }

    private fun estimatedAmountcalculate() {
         balanceET = findViewById(R.id.Balancetv)
        EstimatedEt = findViewById(R.id.Estimated_Amount)
         remainingET = findViewById(R.id.RemainingET)
         paidET= findViewById(R.id.PaidET)

        balanceET.setOnClickListener {view->

            if(remainingET.visibility==View.GONE){
                remainingET.visibility=View.VISIBLE
                paidET.visibility=View.VISIBLE
            }
            else{
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
                balanceET.text="Balance:${EstimatedEt.text}"

            }

        })

    }

    @SuppressLint("MissingInflatedId")
    private fun showCategoryPopup() {
        val spinnerAdapter = BudgetCategoryAdapter(this, spinnerItems)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Select Category")
            .setAdapter(spinnerAdapter) { _, position ->
                val selectedItem = spinnerItems[position]
               categoryButton.text=selectedItem.text
//                categoryButton.setCompoundDrawablesWithIntrinsicBounds(selectedItem.imageResId, 0, 0, 0)

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
            else->super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}