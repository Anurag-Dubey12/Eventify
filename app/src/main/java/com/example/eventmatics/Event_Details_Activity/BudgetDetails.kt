package com.example.eventmatics.Event_Details_Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.data_class.SpinnerItem
import java.util.Calendar

class BudgetDetails : AppCompatActivity(){
    lateinit var nameEditText:EditText
    lateinit var  balanceET: TextView
    lateinit var  PaidET: TextView
    lateinit var EstimatedEt: EditText
    lateinit var NoteET: EditText
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
        categoryselection = findViewById(R.id.categoryselection)

        categoryselection.setOnClickListener {
            showCategoryPopup()
        }
        EstimatedEt.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(edit: Editable?) {
                balanceET.text="${EstimatedEt.text}"
            }
        })
        val selected_item:Budget?=intent.getParcelableExtra("selected_item")
        if (selected_item!=null){
            nameEditText.setText(selected_item.name)
            categoryselection.setText(selected_item.category)
            NoteET.setText(selected_item.note)
            balanceET.setText(selected_item.balance)
            EstimatedEt.setText(selected_item.estimatedAmount)
        }

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

                  UpdateDatabase(selected_item.id)
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
    private fun UpdateDatabase(id: Long) {
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
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val budget=Budget(id,name,category,note,Totalamount,balance,"","")
        db.updateBudget(budget)
        Toast.makeText(this, "Budget Updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }


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
        val budget=Budget(id.toLong(),name,category,note,Totalamount,balance,"","")
        db.createBudget(budget)
        Toast.makeText(this, "Budget Added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
