package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Event_Extra_info_Activities.Guest
import com.example.eventmatics.R

class GuestDetails : AppCompatActivity() {
    private lateinit var guestNameEt: EditText
    private lateinit var maleButton: AppCompatButton
    private lateinit var femaleButton: AppCompatButton
    private lateinit var adultButton: AppCompatButton
    private lateinit var childButton: AppCompatButton
    private lateinit var babyButton: AppCompatButton
    private lateinit var guestNoteEt: EditText
    private lateinit var invitationSentButton: AppCompatButton
    private lateinit var notSentButton: AppCompatButton
    private lateinit var contactviewtv: ImageView
    private lateinit var guestPhoneEt: EditText
    private lateinit var guestPhonetv: TextView
    private lateinit var guestEmailEt: EditText
    private lateinit var guestEmailtv: TextView
    private lateinit var guestAddresssEt: EditText
    private lateinit var guestAddressstv: TextView
    private lateinit var guestAdd: ImageView
    private lateinit var paymentTransRecyclerView: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_details)
        val vendortoolbar: Toolbar =findViewById(R.id.vendortoolbar)
        setSupportActionBar(vendortoolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        guestNameEt = findViewById(R.id.GuestNameEt)
        maleButton = findViewById(R.id.Malebut)
        femaleButton =findViewById(R.id.femalebut)
        adultButton = findViewById(R.id.Adultbut)
        contactviewtv=findViewById(R.id.contactviewtv)
        childButton = findViewById(R.id.ChildBut)
        babyButton = findViewById(R.id.Babybut)
        guestNoteEt =findViewById(R.id.GuestNoteET)
        invitationSentButton = findViewById(R.id.Invitaionsentbut)
        notSentButton = findViewById(R.id.NotSentBut)
        guestPhoneEt = findViewById(R.id.GuestPhoneEt)
        guestEmailEt = findViewById(R.id.GuestEmailEt)
        guestAddresssEt = findViewById(R.id.GuestAddresssEt)
        guestAdd = findViewById(R.id.GuestAdd)
        paymentTransRecyclerView =findViewById(R.id.paymenttrans)
        guestPhonetv=findViewById(R.id.GuestPhonetv)
        guestEmailtv=findViewById(R.id.GuestEmailtv)
        guestAddressstv=findViewById(R.id.GuestAddressstv)

        contactviewtv.setOnClickListener {
            guestinfoview()
        }
        guestAdd.setOnClickListener {
            Intent(this,Guest::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun guestinfoview() {
        if(guestPhonetv.visibility== View.VISIBLE && guestEmailtv.visibility== View.VISIBLE && guestAddressstv.visibility== View.VISIBLE &&
            guestPhoneEt.visibility== View.VISIBLE && guestEmailEt.visibility== View.VISIBLE && guestAddresssEt.visibility== View.VISIBLE){

            guestPhonetv.visibility=View.GONE
            guestEmailtv.visibility=View.GONE
            guestAddressstv.visibility=View.GONE
            guestPhoneEt.visibility=View.GONE
            guestEmailEt.visibility=View.GONE
            guestAddresssEt.visibility=View.GONE
        }
        else{
            guestPhonetv.visibility=View.VISIBLE
            guestEmailtv.visibility=View.VISIBLE
            guestAddressstv.visibility=View.VISIBLE
            guestPhoneEt.visibility=View.VISIBLE
            guestEmailEt.visibility=View.VISIBLE
            guestAddresssEt.visibility=View.VISIBLE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.guest_vendor_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            R.id.contacts->{
                Intent(Intent.ACTION_PICK).also {
                    it.type=ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                    startActivityForResult(it,900)
                }
                true
            }
        else->super.onOptionsItemSelected(item)
    }
}

    //retriveing the contact name and number form the device
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 900) {
            val contactUri = data?.data ?: return
            val contactInfo = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            val contentResolver: ContentResolver = applicationContext.contentResolver
            val cursor = contentResolver.query(contactUri, contactInfo, null, null, null)

            cursor?.let {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    val contactName = it.getString(nameIndex)
                    val contactNumber = it.getString(numberIndex)

                    guestNameEt.setText(contactName)
                    guestPhoneEt.setText(contactNumber)
                }
                it.close()
            }
        }
    }
}