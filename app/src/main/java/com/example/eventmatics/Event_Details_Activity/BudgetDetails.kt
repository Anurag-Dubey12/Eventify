package com.example.eventmatics.Event_Details_Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.PaymentActivityAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Paymentinfo
import com.example.eventmatics.data_class.SpinnerItem
import com.example.eventmatics.fragments.BudgetFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar

class BudgetDetails : AppCompatActivity(),
    PaymentActivityAdapter.OnItemClickListener{
    lateinit var nameEditText:EditText
    lateinit var  balanceET: TextView
    lateinit var  totalPayment: TextView
    lateinit var  PaymentRecycler: RecyclerView
    lateinit var  Paymentadd: ImageView
    lateinit var  addapyment: LinearLayout
    lateinit var  warning_Message: LinearLayout
    lateinit var  adapter: PaymentActivityAdapter
    val paymentlist: MutableList<Paymentinfo> = mutableListOf()
    val updatedpaymentlist: MutableList<Paymentinfo> = mutableListOf()
    val paymentSet: MutableSet<Paymentinfo> = mutableSetOf()

    lateinit var EstimatedEt: EditText
    val fragmentManager = supportFragmentManager
    lateinit var NoteET: EditText
    lateinit var budgetFragment: BudgetFragment
    lateinit var PaymentBalance: LinearLayout
    lateinit var categoryselection: TextView


    //Payment ID
    private lateinit var etName: EditText
    private lateinit var etAmount: EditText
    private lateinit var buttonPending: MaterialButton
    private lateinit var buttonPaid: MaterialButton
    private lateinit var etDate: TextView
    private lateinit var buttonSubmit: MaterialButton
    private var isPaid: Boolean = false
    private var isButtonClicked: Boolean = false
    private var status: String = ""
    private var updatedStatus: String = ""
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

    private fun showBudgetFragment(payment: Paymentinfo?) {
        updatepaymentsheet(payment)
    }

    override fun onItemClick(payment: Paymentinfo) {
        showBudgetFragment(payment)
        Log.d("PaymentDetails","The Detials Are:$payment")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_details)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nameEditText = findViewById(R.id.NameET)
        EstimatedEt = findViewById(R.id.Estimated_Amount)
        NoteET = findViewById(R.id.NoteET)
        totalPayment = findViewById(R.id.totalPayment)
        PaymentRecycler = findViewById(R.id.PaymentRecycler)
        Paymentadd = findViewById(R.id.paymentadd)
        balanceET = findViewById(R.id.Balancetv)
        warning_Message = findViewById(R.id.warning_Message)
        PaymentBalance = findViewById(R.id.PaymentBalance)
        addapyment = findViewById(R.id.addapyment)
        categoryselection = findViewById(R.id.categoryselection)

        categoryselection.setOnClickListener {
            showCategoryPopup()
        }
        val selected_item: Budget?=intent.getParcelableExtra("selected_item")

        if (selected_item!=null){
            PaymentBalance.visibility=View.VISIBLE
            addapyment.visibility=View.VISIBLE
            PaymentRecycler.visibility=View.VISIBLE
            nameEditText.setText(selected_item.name)
            categoryselection.setText(selected_item.category)
            NoteET.setText(selected_item.note)
            balanceET.setText(selected_item.balance)
            EstimatedEt.setText(selected_item.estimatedAmount)

            val id = selected_item.id.toInt()
            val databasehelper = DatabaseManager.getDatabase(this)
            val paymentData = databasehelper.getPaymentsForBudget(id)
            for (payment in paymentData) {
                Log.d("PaymentData", "ID: ${payment.id}, Name: ${payment.name}, Amount: ${payment.amount}")
            }
            paymentSet.clear()
            paymentSet.addAll(paymentData)
            val paymentList = paymentSet.toList()
            adapter = PaymentActivityAdapter(this, paymentList.toMutableList(),this)
            PaymentRecycler.adapter = adapter
            PaymentRecycler.layoutManager = LinearLayoutManager(this)

            val totalPaymentAmount =databasehelper.getTotalPaymentAmount()
            totalPayment.setText(totalPaymentAmount.toString())
            Log.d("payment","The total amount is :$totalPaymentAmount")
            val estimatedAmount = EstimatedEt.text.toString().toFloatOrNull() ?: 0.0f
            val balance = estimatedAmount - totalPaymentAmount
            balanceET.text = balance.toString()
            if(totalPaymentAmount>balance){
                warning_Message.visibility=View.VISIBLE
                balanceET.setTextColor(ContextCompat.getColor(this,R.color.Red))
            }
        }

        Paymentadd.setOnClickListener {
            showpaymentsheet()
        }

    }
private fun updatepaymentsheet(payment: Paymentinfo?){
    val dialogview=BottomSheetDialog(this)
    dialogview.setContentView(R.layout.fragment_budget)
    dialogview.show()
    etName = dialogview.findViewById(R.id.editTextName)!!
    etAmount = dialogview.findViewById(R.id.editTextAmount)!!
    buttonPending = dialogview.findViewById(R.id.buttonPending)!!
    buttonPaid = dialogview.findViewById(R.id.buttonPaid)!!
    etDate = dialogview.findViewById(R.id.editTextDate)!!
    buttonSubmit = dialogview.findViewById(R.id.buttonSubmit)!!

    if (payment != null) {
        etName.setText(payment.name.toString())
        etAmount.setText(payment.amount.toString())
        etDate.text = payment.date
        if (payment?.status == "Paid") {
            setButtonBackground(buttonPaid, true)
            setButtonBackground(buttonPending, false)
        } else {
            setButtonBackground(buttonPaid, false)
            setButtonBackground(buttonPending, true)
        }
    }

    buttonPending.setOnClickListener {
        isPaid = false
        isButtonClicked = true
        setButtonBackground(buttonPending, true)
        setButtonBackground(buttonPaid, false)
    }

    buttonPaid.setOnClickListener {
        isPaid = true
        isButtonClicked = true
        setButtonBackground(buttonPaid, true)
        setButtonBackground(buttonPending, false)
    }

    buttonSubmit.setOnClickListener {
        val selected_item: Budget?=intent.getParcelableExtra("selected_item")
        val name = etName.text.toString()
        val amount = etAmount.text.toString().toFloat()
        val date = etDate.text.toString()
        status = if (isPaid) {
            "Paid"
        } else {
            "Pending"
        }
        val payment = Paymentinfo(payment!!.id, name, amount, date, status,selected_item!!.id)
        paymentlist.add(payment)
        adapter.notifyDataSetChanged()
        dialogview.dismiss()
    }
    etDate.setOnClickListener { showDatePicker() }
}
    private fun showpaymentsheet() {
        val dialogview=BottomSheetDialog(this)
        dialogview.setContentView(R.layout.fragment_budget)
        dialogview.show()
        etName = dialogview.findViewById(R.id.editTextName)!!
        etAmount = dialogview.findViewById(R.id.editTextAmount)!!
        buttonPending = dialogview.findViewById(R.id.buttonPending)!!
        buttonPaid = dialogview.findViewById(R.id.buttonPaid)!!
        etDate = dialogview.findViewById(R.id.editTextDate)!!
        buttonSubmit = dialogview.findViewById(R.id.buttonSubmit)!!
        buttonPending.setOnClickListener {
            isPaid = false
            isButtonClicked = true
            setButtonBackground(buttonPending, true)
            setButtonBackground(buttonPaid, false)
        }

        buttonPaid.setOnClickListener {
            isPaid = true
            isButtonClicked = true
            setButtonBackground(buttonPaid, true)
            setButtonBackground(buttonPending, false)
        }

        buttonSubmit.setOnClickListener {
            val selected_item: Budget?=intent.getParcelableExtra("selected_item")
            val name = etName.text.toString()
            val amount = etAmount.text.toString().toFloat()
            val date = etDate.text.toString()
            status = if (isPaid) {
                "Paid"
            } else {
                "Pending"
            }
            val payment = Paymentinfo(0, name, amount, date, status,selected_item!!.id)
            paymentlist.add(payment)
            adapter.notifyDataSetChanged()
            dialogview.dismiss()

        }
        etDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val constraint = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraint.build())
            .build()
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDate
            val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val selectedMonth = selectedCalendar.get(Calendar.MONTH)
            val selectedYear = selectedCalendar.get(Calendar.YEAR)
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            etDate.setText(formattedDate)
        }
        datePicker.show(fragmentManager, "datePicker")
    }

    private fun setButtonBackground(button: Button, isSelected: Boolean) {
        val backgroundColor = if (isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList = ContextCompat.getColorStateList(this, backgroundColor)
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
              val selected_item: Budget?=intent.getParcelableExtra("selected_item")
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
        val db= DatabaseManager.getDatabase(this)
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
                val existinguser=db.getPaymentsForBudget(payment.id)
                if(existinguser!=null){
                    db.updatePayment(payment.id,payment)
                }else{
                db.createPayment(payment)

                }
            }
        }

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
        val db= DatabaseManager.getDatabase(this)
        val budget= Budget(id.toLong(),name,category,note,Totalamount,balance,"","Not Paid")
        db.createBudget(budget)
        Toast.makeText(this, "Budget Added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
