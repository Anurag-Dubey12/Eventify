package com.example.eventmatics.Event_Details_Activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.eventmatics.Adapter.CategoryAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Vendor
import com.example.eventmatics.data_class.SpinnerItem

class VendorDetails : AppCompatActivity(){

//    val fragmentmanager:FragmentManager=supportFragmentManager
    private lateinit var vendorNameET: EditText
    private lateinit var categoryButton: Button
    private lateinit var vendorNoteET: EditText
    private lateinit var vendorEstimatedAmount: EditText
    private lateinit var vendorBalanceTV: TextView
    private lateinit var vendorViewTV: ImageView
    private lateinit var vendorPhoneTV: TextView
    private lateinit var vendorPhoneET: EditText
    private lateinit var vendorEmailTV: TextView
    private lateinit var vendorEmailET: EditText
    private lateinit var vendorWebsiteTV: TextView
    private lateinit var vendorWebsiteET: EditText
    private lateinit var vendorAddressTV: TextView
    private lateinit var vendorAddressET: EditText

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
        setContentView(R.layout.activity_vendor_details)

        val vendortoolbar: Toolbar = findViewById(R.id.vendortoolbar)
        setSupportActionBar(vendortoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vendorNameET = findViewById(R.id.VendorNameET)
        categoryButton = findViewById(R.id.vendorcategory_button)
        vendorNoteET = findViewById(R.id.VendorNoteET)
        vendorEstimatedAmount = findViewById(R.id.VendorEstimated_Amount)
        vendorBalanceTV = findViewById(R.id.VendorBalancetv)
        vendorViewTV = findViewById(R.id.Vendorviewtv)
        vendorPhoneTV = findViewById(R.id.VendorPhonetv)
        vendorPhoneET = findViewById(R.id.VendortPhoneEt)
        vendorEmailTV = findViewById(R.id.VendorEmailtv)
        vendorEmailET = findViewById(R.id.VendorEmailEt)
        vendorWebsiteTV = findViewById(R.id.Vendorwebsitetv)
        vendorWebsiteET = findViewById(R.id.VendorwebsiteEt)
        vendorAddressTV = findViewById(R.id.VendorAddressstv)
        vendorAddressET = findViewById(R.id.VendorAddresssEt)

        categoryButton.setOnClickListener {
            showvendorcategory()
        }
        vendorViewTV.setOnClickListener {
            infoshow()
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

        val Selected_Item:Vendor?=intent.getParcelableExtra("Selected_Item")
        if(Selected_Item!=null){
            vendorNameET.setText(Selected_Item.name)
            categoryButton.setText(Selected_Item.category)
            vendorNoteET.setText(Selected_Item.note)
            vendorEstimatedAmount.setText(Selected_Item.estimatedAmount)
            vendorBalanceTV.setText(Selected_Item.balance)
            vendorPhoneET.setText(Selected_Item.balance)
            vendorEmailET.setText(Selected_Item.emailid)
            vendorWebsiteET.setText(Selected_Item.website)
            vendorAddressET.setText(Selected_Item.address)
        }
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
                    val Selected_Item:Vendor?=intent.getParcelableExtra("Selected_Item")
                    if(Selected_Item!=null){
                        UpdateDatabase(Selected_Item.id)
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
        val vendor=Vendor(0,vendorName,category,vendorNote,estimatedAmount,vendorBalance,"","",vendorPhone,vendorEmail,vendorWebsite,vendorAddress)
        val db=LocalDatabase(this,databasename)
        db.createVendor(vendor)
        Toast.makeText(this, "Vendor Added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
    private fun UpdateDatabase(id: Long) {
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
        val vendor=Vendor(id,vendorName,category,vendorNote,estimatedAmount,vendorBalance,"","",vendorPhone,vendorEmail,vendorWebsite,vendorAddress)
        val db=LocalDatabase(this,databasename)
        db.updateVendor(vendor)
        Toast.makeText(this, "Vendor Updated successfully", Toast.LENGTH_SHORT).show()
        finish()
    }
}