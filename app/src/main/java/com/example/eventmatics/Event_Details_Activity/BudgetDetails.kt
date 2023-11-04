package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
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
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.PaymentActivityAdapter
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.BudgetEntity
import com.example.eventmatics.RoomDatabase.DataClas.PaymentEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Paymentinfo
import com.example.eventmatics.fragments.BudgetFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    val paymentlist: MutableList<PaymentEntity> = mutableListOf()
    val updatedpaymentlist: MutableList<Paymentinfo> = mutableListOf()
    val paymentSet: MutableSet<PaymentEntity> = mutableSetOf()
    lateinit var EstimatedEt: EditText
    val fragmentManager = supportFragmentManager
    lateinit var NoteET: EditText
    lateinit var budgetFragment: BudgetFragment
    lateinit var PaymentBalance: LinearLayout
    lateinit var categoryselection: ImageView
    lateinit var categoryedit: TextView
    private var currentBudget: BudgetEntity? = null
    
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
    val spinnerItems = arrayOf(
        "Accessories", "Accommodation", "Attire & accessories",
        "Ceremony", "Flower & Decor", "Health & Beauty", "Jewelry",
        "Miscellaneous", "Music & Show", "Photo & Video", "Reception", "Transportation")

    private fun showBudgetFragment(payment: PaymentEntity?) {
        updatepaymentsheet(payment)
    }
    override fun onItemClick(payment: PaymentEntity) {
        showBudgetFragment(payment)
        Log.d("PaymentDetails","The Detials Are:$payment")
    }
    @SuppressLint("MissingInflatedId")
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
        categoryedit = findViewById(R.id.categoryedit)
        balanceET = findViewById(R.id.Balancetv)
        warning_Message = findViewById(R.id.warning_Message)
        PaymentBalance = findViewById(R.id.PaymentBalance)
        addapyment = findViewById(R.id.addapyment)
        categoryselection = findViewById(R.id.categoryselection)

        categoryselection.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Select Category")
                .setSingleChoiceItems(spinnerItems, 0) { dialog, which ->
                    categoryedit.text=spinnerItems[which]
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss() }
                .show() }

        val selected_item: BudgetEntity? = intent.getParcelableExtra("selected_item")
        if (selected_item != null) {
            currentBudget=selected_item
            PaymentBalance.visibility = View.VISIBLE
            addapyment.visibility = View.VISIBLE
            PaymentRecycler.visibility = View.VISIBLE
            nameEditText.setText(selected_item.name)
            categoryedit.setText(selected_item.category)
            NoteET.setText(selected_item.note)
            balanceET.setText(selected_item.balance)
            EstimatedEt.setText(selected_item.estimatedAmount)

            val id = selected_item.id.toInt()
            val dao = RoomDatabaseManager.getEventsDao(applicationContext)

            GlobalScope.launch(Dispatchers.IO) {
                val paymentData = dao.getPaymentsForBudget(id.toInt())
                paymentSet.clear()
                paymentSet.addAll(paymentData)

                val totalPaymentAmount = dao.getTotalPaymentAmount(selected_item.id.toInt().toLong())

                val estimatedAmount = EstimatedEt.text.toString().toFloatOrNull() ?: 0.0f
                val balance = estimatedAmount - totalPaymentAmount

                runOnUiThread {
                    val paymentList = paymentSet.toList()
                    adapter = PaymentActivityAdapter(applicationContext, paymentList.toMutableList(), this@BudgetDetails)
                    PaymentRecycler.adapter = adapter
                    PaymentRecycler.layoutManager = LinearLayoutManager(this@BudgetDetails)

                    totalPayment.setText(totalPaymentAmount.toString())
                    balanceET.text = balance.toString()

                    if (totalPaymentAmount > estimatedAmount) {
                        warning_Message.visibility = View.VISIBLE
                        balanceET.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red))
                    }
                }
            }
        }

        Paymentadd.setOnClickListener { showpaymentsheet() }
    }
private fun updatepaymentsheet(payment: PaymentEntity?){
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
        GlobalScope.launch(Dispatchers.IO){
        val selected_item: BudgetEntity?=intent.getParcelableExtra("selected_item")
        val name = etName.text.toString()
        val amount = etAmount.text.toString().toFloat()
            val date = etDate.text.toString()
            runOnUiThread {
        status =if(!isButtonClicked){ "${payment?.status}" }
        else if (isPaid) { "Paid" }
        else { "Pending" }
        val payment = PaymentEntity(payment!!.id, name, amount, date, status,selected_item!!.id)
        paymentlist.add(payment)
        adapter = PaymentActivityAdapter(applicationContext, paymentlist,this@BudgetDetails)
        PaymentRecycler.adapter = adapter
        PaymentRecycler.layoutManager = LinearLayoutManager(this@BudgetDetails)
        adapter.notifyDataSetChanged()
        dialogview.dismiss()
            }
    }
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
        GlobalScope.launch(Dispatchers.IO){
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        val event=dao.getEventData(1)
        val date=event!!.date
        etDate.text=date
        }

        buttonSubmit.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
            val selected_item: BudgetEntity?=intent.getParcelableExtra("selected_item")
            val name = etName.text.toString()
            val amount = etAmount.text.toString().toFloat()
            val date = etDate.text.toString()
                runOnUiThread {
            status = if (isPaid) { "Paid" }
            else { "Pending" }
            val payment = PaymentEntity(0, name, amount, date, status,selected_item!!.id)
            paymentlist.add(payment)
            adapter = PaymentActivityAdapter(applicationContext, paymentlist,this@BudgetDetails)
            PaymentRecycler.adapter = adapter
            PaymentRecycler.layoutManager = LinearLayoutManager(this@BudgetDetails)
            adapter.notifyDataSetChanged()
                    updateBalance()
            dialogview.dismiss()
        }
    }
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
               onBackPressed()
                true
            }
          R.id.Check->{
              val selected_item: BudgetEntity?=intent.getParcelableExtra("selected_item")
              if (selected_item!=null){ UpdateDatabase(selected_item.id,paymentlist) }
              else{ AddValueToDataBase() }
              true
          }
            else->super.onOptionsItemSelected(item)
        }
    }
    private fun UpdateDatabase(id: Long, paymentList: MutableList<PaymentEntity>) {
        val name = nameEditText.text.toString()
        val totalamt = EstimatedEt.text.toString().toFloat()
        val Totalamount = totalamt.toString()
        val note = NoteET.text.toString()
        val category = categoryedit.text.toString()
        val balance = balanceET.text.toString()

        if (name.isEmpty()) {
            nameEditText.error = "Please enter a name"
            return
        }

        if (totalamt == 0f) {
            EstimatedEt.error = "Please enter an amount"
            return
        }

        var status: String
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)

        GlobalScope.launch(Dispatchers.IO) {
            val isBudgetPaid = dao.isBudgetPaid(id)
            if (isBudgetPaid) {
                status = "Paid"
            } else {
                status = "Not Paid"
            }
            val budget = BudgetEntity(id, name, category, note, Totalamount, balance, "", status)
            dao.updateBudget(budget)

            for (payment in paymentList) {
                val existingPayments = dao.getPaymentsForBudget(id.toInt())
                val existingPayment = existingPayments.find { it.id == payment.id }
                if (existingPayment != null) {
                    dao.updatePayment(payment.id, payment.name, payment.amount.toDouble(), payment.date, payment.status, id.toInt())

                } else {
                    dao.InsertPayment(payment)
                }
            }
            val paidamt = dao.getTotalPaymentAmount(id)

            if (paidamt.toFloat() >= Totalamount.toFloat()) {
                dao.updateBudgetPaid(budget.id, "Paid")
            } else {
                dao.updateBudgetPaid(budget.id, "Not Paid")
            }
            finish()
        }
    }
    private fun updateBalance() {
        GlobalScope.launch(Dispatchers.IO){

        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        val estimatedAmount = EstimatedEt.text.toString().toFloatOrNull() ?: 0.0f
        val totalPaymentAmount = dao.getTotalPaymentAmount(currentBudget!!.id)

        val balance = estimatedAmount - totalPaymentAmount
        balanceET.text = balance.toString()

        if (totalPaymentAmount > estimatedAmount) {
            warning_Message.visibility = View.VISIBLE
            balanceET.setTextColor(ContextCompat.getColor(applicationContext, R.color.Red))
        } else {
            warning_Message.visibility = View.GONE
            balanceET.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        }
        }
    }
    private fun AddValueToDataBase() {
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        val name = nameEditText.text.toString()
        val totalamt = EstimatedEt.text.toString().toFloat()
        val Totalamount=totalamt.toString()
        val note=NoteET.text.toString()
        val category=categoryedit.text.toString()
        val balance=balanceET.text.toString()
        if (name.isEmpty()) { nameEditText.error = "Please enter a name" }
        if (totalamt==0f) { EstimatedEt.error = "Please enter an amount" }
        GlobalScope.launch(Dispatchers.IO){
        val id=0
        val budget= BudgetEntity(id.toLong(),name,category,note,Totalamount,balance,"","Not Paid")
        dao.InsertBudget(budget)
        finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.budget_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
