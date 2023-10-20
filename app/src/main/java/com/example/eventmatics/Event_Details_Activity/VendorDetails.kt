package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.VendorPaymentActivityAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseManager
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Vendor
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.VendorPaymentinfo
import com.example.eventmatics.fragments.VendorFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar

class VendorDetails : AppCompatActivity(),
    VendorPaymentActivityAdapter.OnItemClickListener{

    val fragmentManager = supportFragmentManager
    private lateinit var vendorNameET: EditText
    private lateinit var categoryButton: ImageView
    private lateinit var vendorNoteET: EditText
    private lateinit var vendorEstimatedAmount: EditText
    private lateinit var vendorBalanceTV: TextView
    private lateinit var categoryedit: TextView
    private lateinit var vendorViewTV: ImageView
    private lateinit var PaymentAdd: ImageView
    private lateinit var ContactDetails: CardView
    private lateinit var vendorPhoneTV: TextView
    private lateinit var vendorPhoneET: EditText
    private lateinit var vendorEmailTV: TextView
    private lateinit var totalPayment: TextView
    private lateinit var vendorEmailET: EditText
    private lateinit var vendorWebsiteTV: TextView
    private lateinit var Balancefeild: LinearLayout
    private lateinit var paymentLayout: LinearLayout
    private lateinit var paymentDetails: LinearLayout
    private lateinit var warning_Message: LinearLayout
    private lateinit var vendorWebsiteET: EditText
    private lateinit var vendorAddressTV: TextView
    private lateinit var vendorpaymenttrans: RecyclerView
    private lateinit var vendorAddressET: EditText
    var paymentlist:MutableList<VendorPaymentinfo> = mutableListOf()
    var paymentset:MutableSet<VendorPaymentinfo> = mutableSetOf()
    lateinit var adapter: VendorPaymentActivityAdapter
    lateinit var vendorFragment:VendorFragment

    //Payment Id
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var vendorButtonPending: AppCompatButton
    private lateinit var vendorButtonPaid: AppCompatButton
    private lateinit var vendorexpireDate: TextView
    private lateinit var buttonSubmit: MaterialButton
    private var isPaid:Boolean=false
    private var isButtonClicked:Boolean=false
    var paymentStatus :String=" "
    var updatepaymentStatus :String=" "

    val spinnerItems = arrayOf(
        "Accessories",
        "Accommodation",
        "Attire & accessories",
        "Ceremony",
        "Flower & Decor",
        "Health & Beauty",
        "Jewelry",
        "Miscellaneous",
        "Music & Show",
        "Photo & Video",
        "Reception",
        "Transportation"
    )

    fun showVendorFragment(payment: VendorPaymentinfo){
       updatepaymentsheet(payment)
    }
    override fun onitemclick(paymentList: VendorPaymentinfo) {
       showVendorFragment(paymentList)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_details)

        val vendortoolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(vendortoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vendorNameET = findViewById(R.id.VendorNameET)
        categoryButton = findViewById(R.id.vendorcategory_button)
        vendorNoteET = findViewById(R.id.VendorNoteET)
        paymentDetails = findViewById(R.id.paymentDetails)
        categoryedit = findViewById(R.id.categoryedit)
        paymentLayout = findViewById(R.id.paymentLayout)
        vendorEstimatedAmount = findViewById(R.id.VendorEstimated_Amount)
        vendorBalanceTV = findViewById(R.id.VendorBalancetv)
        vendorViewTV = findViewById(R.id.Vendorviewtv)
        ContactDetails = findViewById(R.id.ContactDetails)
        vendorPhoneTV = findViewById(R.id.VendorPhonetv)
        totalPayment = findViewById(R.id.totalPayment)
        vendorpaymenttrans = findViewById(R.id.vendorpaymenttrans)
        warning_Message = findViewById(R.id.warning_Message)
        vendorPhoneET = findViewById(R.id.VendortPhoneEt)
        vendorEmailTV = findViewById(R.id.VendorEmailtv)
        PaymentAdd = findViewById(R.id.PaymentAdd)
        vendorEmailET = findViewById(R.id.VendorEmailEt)
        vendorWebsiteTV = findViewById(R.id.Vendorwebsitetv)
        vendorWebsiteET = findViewById(R.id.VendorwebsiteEt)
        Balancefeild = findViewById(R.id.Balancefeild)
        vendorAddressTV = findViewById(R.id.VendorAddressstv)
        vendorAddressET = findViewById(R.id.VendorAddresssEt)

        categoryButton.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Select Category")
                .setSingleChoiceItems(spinnerItems, 0) { dialog, which ->
                    categoryedit.text=spinnerItems[which]
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        vendorViewTV.setOnClickListener { infoshow() }
        val Selected_Item: Vendor?=intent.getParcelableExtra("Selected_Item")
        if(Selected_Item!=null){
            Balancefeild.visibility=View.VISIBLE
            paymentLayout.visibility=View.VISIBLE
            paymentDetails.visibility=View.VISIBLE
            vendorNameET.setText(Selected_Item.name)
            categoryedit.setText(Selected_Item.category)
            vendorNoteET.setText(Selected_Item.note)
            vendorEstimatedAmount.setText(Selected_Item.estimatedAmount)
            vendorBalanceTV.setText(Selected_Item.balance)
            vendorPhoneET.setText(Selected_Item.phonenumber)
            vendorEmailET.setText(Selected_Item.emailid)
            vendorWebsiteET.setText(Selected_Item.website)
            vendorAddressET.setText(Selected_Item.address)

            val id=Selected_Item?.id!!.toInt()
            val db=DatabaseManager.getDatabase(this)
            val Payment=db.getPaymentForVendor(id)
            paymentset.clear()
            paymentset.addAll(Payment)
            val PaymentList=paymentset.toList()
           adapter= VendorPaymentActivityAdapter(this,PaymentList.toMutableList(),this)
            vendorpaymenttrans.layoutManager=LinearLayoutManager(this)
            vendorpaymenttrans.adapter=adapter
            paymentlist.clear()
            paymentlist.addAll(Payment)
            val totalamt=db.getTotalPaymentAmountVendor(id.toInt())
            totalPayment.text=totalamt.toString()
            val estimatedamt=vendorEstimatedAmount.text.toString().toFloatOrNull()?:0.0f
            val balance=estimatedamt-totalamt
            vendorBalanceTV.setText(balance.toString())
            if(totalamt>estimatedamt){
                warning_Message.visibility=View.VISIBLE
                vendorBalanceTV.setTextColor(ContextCompat.getColor(this,R.color.Red))
            }
            adapter.notifyDataSetChanged()
        }
        adapter= VendorPaymentActivityAdapter(this,paymentlist,this)

        PaymentAdd.setOnClickListener {
            showpaymentsheet()
        }

    }
@SuppressLint("SuspiciousIndentation")
private fun showpaymentsheet(){
    val dialogview= BottomSheetDialog(this)
    dialogview.setContentView(R.layout.fragment_vendor)
    dialogview.show()
    editTextName = dialogview.findViewById(R.id.editTextName)!!
    editTextAmount = dialogview.findViewById(R.id.editTextAmount)!!
    vendorButtonPending = dialogview.findViewById(R.id.vendorbuttonPending)!!
    vendorButtonPaid = dialogview.findViewById(R.id.vendorbuttonPaid)!!
    vendorexpireDate = dialogview.findViewById(R.id.editTextDate)!!
    buttonSubmit = dialogview.findViewById(R.id.buttonSubmit)!!
    vendorButtonPending.setOnClickListener {
        isPaid=false
        isButtonClicked=true
        setButtonBackground(vendorButtonPending,true)
        setButtonBackground(vendorButtonPaid,false)
    }

    vendorButtonPaid.setOnClickListener {
        isButtonClicked=true
        isPaid=true
        setButtonBackground(vendorButtonPaid,true)
        setButtonBackground(vendorButtonPending,false)
    }
    vendorexpireDate.setOnClickListener {
        showDatePicker()
    }
    buttonSubmit.setOnClickListener {
        val Selected_Item: Vendor?=intent.getParcelableExtra("Selected_Item")
            val name=editTextName.text.toString()
            val amount=editTextAmount.text.toString().toFloat()
            val date=vendorexpireDate.text.toString()
            if(isPaid){
                paymentStatus="Paid"
            }
            else{
                paymentStatus="Pending"
            }
            val payment= VendorPaymentinfo(0,name,amount,date,paymentStatus, Selected_Item?.id!!)
            paymentlist.add(payment)
        adapter= VendorPaymentActivityAdapter(this,paymentlist,this)
        vendorpaymenttrans.layoutManager=LinearLayoutManager(this)
        vendorpaymenttrans.adapter=adapter
            Toast.makeText(this,"Data Added",Toast.LENGTH_SHORT).show()
            dialogview.dismiss()
        }


}
    private fun updatepaymentsheet(payment: VendorPaymentinfo) {
        val dialogview= BottomSheetDialog(this)
        dialogview.setContentView(R.layout.fragment_vendor)
        dialogview.show()
        editTextName = dialogview.findViewById(R.id.editTextName)!!
        editTextAmount = dialogview.findViewById(R.id.editTextAmount)!!
        vendorButtonPending = dialogview.findViewById(R.id.vendorbuttonPending)!!
        vendorButtonPaid = dialogview.findViewById(R.id.vendorbuttonPaid)!!
        vendorexpireDate = dialogview.findViewById(R.id.editTextDate)!!
        buttonSubmit = dialogview.findViewById(R.id.buttonSubmit)!!

        if(payment!=null){
            editTextName.setText(payment.name.toString())
            editTextAmount.setText(payment.amount.toString())
            vendorexpireDate.text = payment.date

            if (payment?.status == "Paid") {
                setButtonBackground(vendorButtonPending,false)
                setButtonBackground(vendorButtonPaid,true)
            } else {
                setButtonBackground(vendorButtonPending,true)
                setButtonBackground(vendorButtonPaid,false)
            }
        }
        vendorButtonPending.setOnClickListener {
            isPaid=false
            isButtonClicked=true
            setButtonBackground(vendorButtonPending,true)
            setButtonBackground(vendorButtonPaid,false)
        }

        vendorButtonPaid.setOnClickListener {
            isButtonClicked=true
            isPaid=true
            setButtonBackground(vendorButtonPaid,true)
            setButtonBackground(vendorButtonPending,false)
        }
        vendorexpireDate.setOnClickListener {
            showDatePicker()
        }
        buttonSubmit.setOnClickListener {
            val Selected_Item: Vendor?=intent.getParcelableExtra("Selected_Item")
            val name=editTextName.text.toString()
            val amount=editTextAmount.text.toString().toFloat()
            val date=vendorexpireDate.text.toString()
            paymentStatus=if(!isButtonClicked){
                "${payment.status}"
            }else if(isPaid){
                "Paid"
            }
            else{
                "Pending"
            }
            val payment= VendorPaymentinfo(payment.id,name,amount,date,paymentStatus, Selected_Item?.id!!)
            paymentlist.add(payment)
            adapter= VendorPaymentActivityAdapter(this,paymentlist,this)
            vendorpaymenttrans.layoutManager=LinearLayoutManager(this)
            vendorpaymenttrans.adapter=adapter
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"Data Added",Toast.LENGTH_SHORT).show()
            dialogview.dismiss()
        }
    }
    private fun showDatePicker() {
        val constraint= CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

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
            vendorexpireDate.setText(formattedDate)
        }

        datePicker.show(fragmentManager, "datePicker")
    }

    fun setButtonBackground(button:Button,isSelected:Boolean){
        var backgroundcolor=if(isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList= ContextCompat.getColorStateList(this,backgroundcolor)
    }
    private fun infoshow() {
        val isVisible = vendorPhoneTV.visibility == View.VISIBLE
        val visibility = if (isVisible) View.GONE else View.VISIBLE
        val arrowDrawableRes = if (isVisible) R.drawable.drop_arrow else R.drawable.uparrow
        val viewsToToggle = listOf(
            vendorPhoneTV, vendorPhoneET,
            vendorEmailTV, vendorEmailET,
            vendorWebsiteTV, vendorWebsiteET,
            vendorAddressTV, vendorAddressET
        )

        viewsToToggle.forEach { it.visibility = visibility }
        vendorViewTV.setImageResource(arrowDrawableRes)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.guest_vendor_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            //Accessing Contact
            R.id.contacts->{
                Intent(Intent.ACTION_PICK).also {
                    it.type=ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                   startActivityForResult(it,400)
                }
                    true
            }
                R.id.Check->{
                    val Selected_Item: Vendor?=intent.getParcelableExtra("Selected_Item")
                    if(Selected_Item!=null){
                        UpdateDatabase(Selected_Item.id,paymentlist)
                    }else{
                    AddvaluetoDatabase()

                    }
                    true
                }
            else->super.onOptionsItemSelected(item)
            }
        }



    //retrive the contact from the device
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK &&requestCode== 400){
            var contacturi=data?.data ?:return
            var contactinfo= arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                                 ContactsContract.CommonDataKinds.Phone.NUMBER)

            var contentResolver:ContentResolver=applicationContext.contentResolver

            var cursor=contentResolver.query(contacturi,contactinfo,null,null,null)

            cursor?.let {
                if(it.moveToFirst()){
                    var nameindex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    var numberindex=cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    var contactname=it.getString(nameindex)
                    var contactnumber=it.getString(numberindex)
                    vendorNameET.setText(contactname)
                    vendorPhoneET.setText(contactnumber)
                }
            }
        }
    }
    private fun AddvaluetoDatabase() {
        val vendorName = vendorNameET.text.toString()
        val category = categoryedit.text.toString()
        val vendorNote = vendorNoteET.text.toString()
        val estimatedAmount = vendorEstimatedAmount.text.toString()
        val vendorBalance = vendorBalanceTV.text.toString()
        val vendorPhone = vendorPhoneET.text.toString()
        val vendorEmail = vendorEmailET.text.toString()
        val vendorWebsite = vendorWebsiteET.text.toString()
        val vendorAddress = vendorAddressET.text.toString()
        val vendor= Vendor(0,vendorName,category,vendorNote,estimatedAmount,vendorBalance,"","Not Paid",vendorPhone,vendorEmail,vendorWebsite,vendorAddress)
        val db=DatabaseManager.getDatabase(this)
        db.createVendor(vendor)
        Toast.makeText(this, "Vendor Added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun UpdateDatabase(id: Long,paymentList:List<VendorPaymentinfo>) {
        val vendorName = vendorNameET.text.toString()
        val category = categoryedit.text.toString()
        val vendorNote = vendorNoteET.text.toString()
        val estimatedAmount = vendorEstimatedAmount.text.toString()
        val vendorBalance = vendorBalanceTV.text.toString()
        val vendorPhone = vendorPhoneET.text.toString()
        val vendorEmail = vendorEmailET.text.toString()
        val vendorWebsite = vendorWebsiteET.text.toString()
        val vendorAddress = vendorAddressET.text.toString()
        var status:String
        val db=DatabaseManager.getDatabase(this)
        val ispaid=db.isVendorPaid(id)
        if(ispaid){
            status="Paid"
        }
        else{
            status="Not Paid"
        }
        val vendor= Vendor(id,vendorName,category,vendorNote,estimatedAmount,vendorBalance,"",
            status,vendorPhone,vendorEmail,vendorWebsite,vendorAddress)
        db.updateVendor(vendor)
        for(payment in paymentList){
                val existinguser=db.getPaymentForVendor(id.toInt())
                val existingpayment=existinguser.find { it.id==payment.id }
                if(existingpayment!=null){
                    Log.d("PaymentData", "Payment ID ${existingpayment.id} already exists. Updating...")
                    db.updateVendorPayment(payment.id,payment)
                }else{
                    Log.d("PaymentData", "Payment ID ${payment.id} does not exist. Creating...")
                db.createVendorPayment(payment)
                }
        }
        val totalamt=db.getTotalPaymentAmountVendor(id.toInt())
        if(totalamt.toFloat()>=estimatedAmount.toFloat()){
            db.updateVendorPaid(vendor.id,"Paid")
        }else{
            db.updateVendorPaid(vendor.id,"Not Paid")
        }
        Toast.makeText(this, "Vendor Updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}