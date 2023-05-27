package com.example.eventmatics.Event_Details_Activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.Adapter.PaymentActivity
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo
import com.example.eventmatics.data_class.SpinnerItem
import com.example.eventmatics.fragments.VendorFragment

class VendorDetails : AppCompatActivity(),VendorFragment.UserDataListener,VendorFragment.PaidAmountListener,VendorFragment.PendingAmountlistener {

    val fragmentmanager:FragmentManager=supportFragmentManager
    private lateinit var vendorNameET: EditText
    private lateinit var categoryButton: Button
    private lateinit var vendorNoteET: EditText
    private lateinit var vendorEstimatedAmount: EditText
    private lateinit var vendorBalanceTV: TextView
    private lateinit var vendorRemainingET: TextView
    private lateinit var vendorPaidET: TextView
    private lateinit var vendorViewTV: ImageView
    private lateinit var vendorPhoneTV: TextView
    private lateinit var vendorPhoneET: EditText
    private lateinit var vendorEmailTV: TextView
    private lateinit var vendorEmailET: EditText
    private lateinit var vendorWebsiteTV: TextView
    private lateinit var vendorWebsiteET: EditText
    private lateinit var vendorAddressTV: TextView
    private lateinit var vendorAddressET: EditText
    private lateinit var paymentAdd: ImageView
    private lateinit var vendorpaymentTransRecyclerView: RecyclerView
    private lateinit var paymentlist:MutableList<Paymentinfo>
    private lateinit var paymentActivity: PaymentActivity

    val spinnerItems = listOf(
        SpinnerItem(R.drawable.budget_bottom_nav, "Accessories"),
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
        setContentView(R.layout.activity_vendor_details)

        val vendortoolbar: Toolbar = findViewById(R.id.vendortoolbar)
        setSupportActionBar(vendortoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vendorNameET = findViewById(R.id.VendorNameET)
        categoryButton = findViewById(R.id.vendorcategory_button)
        vendorNoteET = findViewById(R.id.VendorNoteET)
        vendorEstimatedAmount = findViewById(R.id.VendorEstimated_Amount)
        vendorBalanceTV = findViewById(R.id.VendorBalancetv)
        vendorRemainingET = findViewById(R.id.VendorRemainingET)
        vendorPaidET = findViewById(R.id.VendorPaidET)
        vendorViewTV = findViewById(R.id.Vendorviewtv)
        vendorPhoneTV = findViewById(R.id.VendorPhonetv)
        vendorPhoneET = findViewById(R.id.VendortPhoneEt)
        vendorEmailTV = findViewById(R.id.VendorEmailtv)
        vendorEmailET = findViewById(R.id.VendorEmailEt)
        vendorWebsiteTV = findViewById(R.id.Vendorwebsitetv)
        vendorWebsiteET = findViewById(R.id.VendorwebsiteEt)
        vendorAddressTV = findViewById(R.id.VendorAddressstv)
        vendorAddressET = findViewById(R.id.VendorAddresssEt)
        paymentAdd = findViewById(R.id.PaymentAdd)

            //Recylerview code
        vendorpaymentTransRecyclerView = findViewById(R.id.vendorpaymenttrans)
        paymentlist= mutableListOf()
        paymentActivity= PaymentActivity(paymentlist)
        vendorpaymentTransRecyclerView.adapter=paymentActivity
        vendorpaymentTransRecyclerView.layoutManager=LinearLayoutManager(this)

        paymentAdd.setOnClickListener {
            showpaymentsheet()
        }
        categoryButton.setOnClickListener {
            showvendorcategory()
        }
        vendorViewTV.setOnClickListener {
            infoshow()
        }
        estimatedAmountcalculate()
    }

    private fun infoshow() {

        if(vendorPhoneTV.visibility== View.VISIBLE && vendorPhoneET.visibility== View.VISIBLE && vendorEmailTV.visibility== View.VISIBLE &&
            vendorEmailET.visibility== View.VISIBLE && vendorWebsiteTV.visibility== View.VISIBLE && vendorWebsiteET.visibility== View.VISIBLE &&
            vendorAddressTV.visibility== View.VISIBLE &&vendorAddressET.visibility== View.VISIBLE ){
            vendorViewTV.setImageResource(R.drawable.up_arrow)
            vendorPhoneTV.visibility= View.GONE
            vendorPhoneET.visibility= View.GONE
            vendorEmailTV.visibility= View.GONE
            vendorEmailET.visibility= View.GONE
            vendorWebsiteTV.visibility= View.GONE
            vendorWebsiteET.visibility= View.GONE
            vendorAddressTV.visibility= View.GONE
            vendorAddressET.visibility= View.GONE
        }
        else{
            vendorViewTV.setImageResource(R.drawable.drop_arrow)
            vendorPhoneTV.visibility= View.VISIBLE
            vendorPhoneET.visibility= View.VISIBLE
            vendorEmailTV.visibility= View.VISIBLE
            vendorEmailET.visibility= View.VISIBLE
            vendorWebsiteTV.visibility= View.VISIBLE
            vendorWebsiteET.visibility= View.VISIBLE
            vendorAddressTV.visibility= View.VISIBLE
            vendorAddressET.visibility= View.VISIBLE
        }

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
    private fun estimatedAmountcalculate() {

        //calculation of Estimated amount with remaing and paid amount
        vendorEstimatedAmount = findViewById(R.id.VendorEstimated_Amount)
        vendorBalanceTV = findViewById(R.id.VendorBalancetv)
        vendorRemainingET = findViewById(R.id.VendorRemainingET)
        vendorPaidET= findViewById(R.id.VendorPaidET)

        val initialres=R.drawable.drop_arrow
        vendorBalanceTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,initialres,0)
        vendorBalanceTV.setOnClickListener {
            if(vendorRemainingET.visibility==View.GONE){
                val newdrawable=R.drawable.up_arrow
                vendorBalanceTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,newdrawable,0)

                vendorRemainingET.visibility=View.VISIBLE
                vendorPaidET.visibility=View.VISIBLE
            }
            else{
                val newdrawable=R.drawable.drop_arrow
                vendorBalanceTV.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,newdrawable,0)
                vendorRemainingET.visibility=View.GONE
                vendorPaidET.visibility=View.GONE
            }
        }
        vendorEstimatedAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(edit: Editable?) {
                vendorBalanceTV.text="Balance:${vendorEstimatedAmount.text}"

            }

        })

    }
    private fun showpaymentsheet() {
        val vendorbottom=VendorFragment()
        vendorbottom.setUserDataListener(this)
        vendorbottom.paidAmountListener=this
        vendorbottom.pendingAmountlistener=this
        vendorbottom.show(fragmentmanager,"bottomsheet")
    }

    override fun onUserDataEntered(userData: Paymentinfo) {
        // Add the entered payment data to the paymentList
        paymentlist.add(userData)
        paymentActivity.notifyDataSetChanged()
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

    override fun onPendingAmountSelected(amount: Float) {
        val displayamount = "Pending: $amount"
        vendorRemainingET.text = displayamount

        var balanceamount = vendorBalanceTV.text.toString()
        balanceamount = balanceamount.substringAfterLast(":") // Remove the "Balance:" prefix
        val balance = balanceamount.toFloat()

        val newBalanceAmount = balance - amount
        val finalAmount = "Balance: $newBalanceAmount"
        vendorBalanceTV.text = finalAmount
    }
    override fun onPaidAmountSelected(amount: Float) {
        val displayamount = "Paid: $amount"
        vendorPaidET.text = displayamount

        var balanceamount = vendorBalanceTV.text.toString()
        balanceamount = balanceamount.substringAfterLast(":") // Remove the "Balance:" prefix
        val totalAmount = balanceamount.toFloat()

        val newBalanceAmount = totalAmount - amount
        val finalAmount = "Balance: $newBalanceAmount"
        vendorBalanceTV.text = finalAmount
    }
}