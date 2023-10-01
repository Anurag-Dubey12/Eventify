package com.example.eventmatics.Event_Details_Activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.VendorPaymentActivityAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Vendor
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.VendorPaymentinfo
import com.example.eventmatics.data_class.SpinnerItem
import com.example.eventmatics.fragments.VendorFragment

class VendorDetails : AppCompatActivity(),VendorFragment.UserDataListener,
    VendorPaymentActivityAdapter.OnItemClickListener,VendorFragment.UserDataUpdateListener{

//    val fragmentmanager:FragmentManager=supportFragmentManager
    private lateinit var vendorNameET: EditText
    private lateinit var categoryButton: Button
    private lateinit var vendorNoteET: EditText
    private lateinit var vendorEstimatedAmount: EditText
    private lateinit var vendorBalanceTV: TextView
    private lateinit var vendorViewTV: ImageView
    private lateinit var PaymentAdd: ImageView
    private lateinit var ContactDetails: CardView
    private lateinit var vendorPhoneTV: TextView
    private lateinit var vendorPhoneET: EditText
    private lateinit var vendorEmailTV: TextView
    private lateinit var vendorEmailET: EditText
    private lateinit var vendorWebsiteTV: TextView
    private lateinit var Balancefeild: LinearLayout
    private lateinit var paymentLayout: LinearLayout
    private lateinit var paymentDetails: LinearLayout
    private lateinit var vendorWebsiteET: EditText
    private lateinit var vendorAddressTV: TextView
    private lateinit var vendorpaymenttrans: RecyclerView
    private lateinit var vendorAddressET: EditText
    var paymentlist:MutableList<VendorPaymentinfo> = mutableListOf()
    var paymentset:MutableSet<VendorPaymentinfo> = mutableSetOf()
    lateinit var adapter: VendorPaymentActivityAdapter
    lateinit var vendorFragment:VendorFragment
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

    override fun onUserDataEntered(userData: VendorPaymentinfo) {
        paymentlist.add(userData)
        adapter.notifyDataSetChanged()
    }
    override fun onuserupdate(userData: VendorPaymentinfo) {
        val db = LocalDatabase(this, getSharedPreference(this, "databasename").toString())
        db.updateVendorPayment(userData.id,userData)
        Log.d("VendorNew","The New Data Are:${userData?.id},${userData?.name}")
    }
    fun showVendorFragment(payment: VendorPaymentinfo){
        val FragmentManagger=supportFragmentManager
        val fragmenttran=FragmentManagger.beginTransaction()
        val Vendor=VendorFragment(this,FragmentManagger,null,payment)
        Vendor.arguments=Bundle().apply {
            putParcelable("VendorPayment",payment)
        }
        Vendor.show(fragmenttran,"VendorFragmenttag")
    }
    override fun onitemclick(paymentList: VendorPaymentinfo) {
       showVendorFragment(paymentList)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_details)

        val vendortoolbar: Toolbar = findViewById(R.id.vendortoolbar)
        setSupportActionBar(vendortoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vendorNameET = findViewById(R.id.VendorNameET)
        categoryButton = findViewById(R.id.vendorcategory_button)
        vendorNoteET = findViewById(R.id.VendorNoteET)
        paymentDetails = findViewById(R.id.paymentDetails)
        paymentLayout = findViewById(R.id.paymentLayout)
        vendorEstimatedAmount = findViewById(R.id.VendorEstimated_Amount)
        vendorBalanceTV = findViewById(R.id.VendorBalancetv)
        vendorViewTV = findViewById(R.id.Vendorviewtv)
        ContactDetails = findViewById(R.id.ContactDetails)
        vendorPhoneTV = findViewById(R.id.VendorPhonetv)
        vendorpaymenttrans = findViewById(R.id.vendorpaymenttrans)
        vendorPhoneET = findViewById(R.id.VendortPhoneEt)
        vendorEmailTV = findViewById(R.id.VendorEmailtv)
        PaymentAdd = findViewById(R.id.PaymentAdd)
        vendorEmailET = findViewById(R.id.VendorEmailEt)
        vendorWebsiteTV = findViewById(R.id.Vendorwebsitetv)
        vendorWebsiteET = findViewById(R.id.VendorwebsiteEt)
        Balancefeild = findViewById(R.id.Balancefeild)
        vendorAddressTV = findViewById(R.id.VendorAddressstv)
        vendorAddressET = findViewById(R.id.VendorAddresssEt)

        categoryButton.setOnClickListener { showvendorcategory() }
        vendorViewTV.setOnClickListener { infoshow() }
        val Selected_Item: Vendor?=intent.getParcelableExtra("Selected_Item")
        if(Selected_Item!=null){
            Balancefeild.visibility=View.VISIBLE
            paymentLayout.visibility=View.VISIBLE
            paymentDetails.visibility=View.VISIBLE
            vendorNameET.setText(Selected_Item.name)
            categoryButton.setText(Selected_Item.category)
            vendorNoteET.setText(Selected_Item.note)
            vendorEstimatedAmount.setText(Selected_Item.estimatedAmount)
            vendorBalanceTV.setText(Selected_Item.balance)
            vendorPhoneET.setText(Selected_Item.phonenumber)
            vendorEmailET.setText(Selected_Item.emailid)
            vendorWebsiteET.setText(Selected_Item.website)
            vendorAddressET.setText(Selected_Item.address)

            val id=Selected_Item?.id!!.toInt()
            val databasename = getSharedPreference(this@VendorDetails, "databasename").toString()
            val db = LocalDatabase(this@VendorDetails, databasename)
            val Payment=db.getPaymentForVendor(id)
            paymentset.clear()
            paymentset.addAll(Payment)
            val PaymentList=paymentset.toList()
           adapter= VendorPaymentActivityAdapter(this,PaymentList.toMutableList(),this)
            vendorpaymenttrans.adapter=adapter
            vendorpaymenttrans.layoutManager=LinearLayoutManager(this)
            paymentlist.clear()
            paymentlist.addAll(Payment)
            adapter.notifyDataSetChanged()
        }
        vendorFragment= VendorFragment(this,supportFragmentManager,Selected_Item?.id,null)
        vendorFragment.setUserDataListener(this)
        adapter= VendorPaymentActivityAdapter(this,paymentlist,this)

        PaymentAdd.setOnClickListener {
            vendorFragment.show(supportFragmentManager,"VendorFragmentManager")
        }

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

    private fun showvendorcategory() {
        val spinneritem=CategoryAdapter(this,spinnerItems)

        val alertdialog=AlertDialog.Builder(this)
            .setTitle("Chose Category")
            .setAdapter(spinneritem){_,position->
                val selecteditem=spinnerItems[position]
                categoryButton.text=selecteditem.text
            }
            .setNegativeButton("cancel",null)
        val dialog=alertdialog.create()
        dialog.show()

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
    fun getSharedPreference(context: Context,key:String):String?{
        val sharedvalue=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }
    private fun AddvaluetoDatabase() {
        val vendorName = vendorNameET.text.toString()
        val category = categoryButton.text.toString()
        val vendorNote = vendorNoteET.text.toString()
        val estimatedAmount = vendorEstimatedAmount.text.toString()
        val vendorBalance = vendorBalanceTV.text.toString()
        val vendorPhone = vendorPhoneET.text.toString()
        val vendorEmail = vendorEmailET.text.toString()
        val vendorWebsite = vendorWebsiteET.text.toString()
        val vendorAddress = vendorAddressET.text.toString()

        val databasename=getSharedPreference(this,"databasename").toString()
        val vendor= Vendor(0,vendorName,category,vendorNote,estimatedAmount,vendorBalance,"","Not Paid",vendorPhone,vendorEmail,vendorWebsite,vendorAddress)
        val db=LocalDatabase(this,databasename)
        db.createVendor(vendor)
        Toast.makeText(this, "Vendor Added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun UpdateDatabase(id: Long,paymentList:List<VendorPaymentinfo>) {
        val vendorName = vendorNameET.text.toString()
        val category = categoryButton.text.toString()
        val vendorNote = vendorNoteET.text.toString()
        val estimatedAmount = vendorEstimatedAmount.text.toString()
        val vendorBalance = vendorBalanceTV.text.toString()
        val vendorPhone = vendorPhoneET.text.toString()
        val vendorEmail = vendorEmailET.text.toString()
        val vendorWebsite = vendorWebsiteET.text.toString()
        val vendorAddress = vendorAddressET.text.toString()
        var status:String
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
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
            if(payment.VendorId  == id.toLong()){
                db.createVendorPayment(payment)
            }
        }
        Toast.makeText(this, "Vendor Updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}