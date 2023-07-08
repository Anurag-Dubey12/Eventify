package com.example.eventmatics.Event_Details_Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.Guest

class GuestDetails : AppCompatActivity() {
    private lateinit var guestNameEt: EditText
    private lateinit var guestNoteEt: EditText
    private lateinit var TotalFamilyMember: EditText
    private lateinit var FeMaleNumber: EditText
    private lateinit var MaleNumber: EditText
    private lateinit var invitationSentButton:  Button
    private lateinit var notSentButton:  Button
    private lateinit var contactviewtv: ImageView
    private lateinit var guestPhoneEt: EditText
    private lateinit var guestPhonetv: TextView
    private lateinit var guestEmailEt: EditText
    private lateinit var guestEmailtv: TextView
    private lateinit var guestAddresssEt: EditText
    private lateinit var guestAddressstv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_details)
        val vendortoolbar: Toolbar =findViewById(R.id.vendortoolbar)
        setSupportActionBar(vendortoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        guestNameEt = findViewById(R.id.GuestNameEt)
        contactviewtv=findViewById(R.id.contactviewtv)
        guestNoteEt =findViewById(R.id.GuestNoteET)
        TotalFamilyMember =findViewById(R.id.TotalFamilyMember)
        FeMaleNumber =findViewById(R.id.FeMaleNumber)
        MaleNumber =findViewById(R.id.MaleNumber)
        invitationSentButton = findViewById(R.id.Invitaionsentbut)
        notSentButton = findViewById(R.id.NotSentBut)
        guestPhoneEt = findViewById(R.id.GuestPhoneEt)
        guestEmailEt = findViewById(R.id.GuestEmailEt)
        guestAddresssEt = findViewById(R.id.GuestAddresssEt)
        guestPhonetv=findViewById(R.id.GuestPhonetv)
        guestEmailtv=findViewById(R.id.GuestEmailtv)
        guestAddressstv=findViewById(R.id.GuestAddressstv)

        invitationSentButton.setOnClickListener {
            setButtonBackground(invitationSentButton,true)
            setButtonBackground(notSentButton,false)
        }
        notSentButton.setOnClickListener {
            setButtonBackground(invitationSentButton,false)
            setButtonBackground(notSentButton,true)
        }
        contactviewtv.setOnClickListener {
            guestinfoview()
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
            R.id.Check->{
                AddValueToDatabase()
                true
            }
        else->super.onOptionsItemSelected(item)
    }
}

    fun getSharedPreference(context: Context, key:String):String?{
        val sharedValues=context.getSharedPreferences("Database",Context.MODE_PRIVATE)
        return sharedValues.getString(key,null)
    }
    private fun AddValueToDatabase() {
        val databasename=getSharedPreference(this,"databasename").toString()
        val db=LocalDatabase(this,databasename)
        val guestName = guestNameEt.text.toString()
        val guestNote = guestNoteEt.text.toString()
        val totalFamilyMembers = TotalFamilyMember.text.toString().toInt()
        val FeMaleNumber = FeMaleNumber.text.toString().toInt()
        val MaleNumber = MaleNumber.text.toString().toInt()
        val guestPhone = guestPhoneEt.text.toString()
        val guestEmail = guestEmailEt.text.toString()
        val guestAddress = guestAddresssEt.text.toString()
        val GuestList= Guest(1,guestName,totalFamilyMembers,MaleNumber,FeMaleNumber,guestNote, " ",
        guestPhone,guestEmail,guestAddress)
        db.createGuest(GuestList)
        Toast.makeText(this, "Guest Added successfully", Toast.LENGTH_SHORT).show()
        finish()
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
    fun setButtonBackground(button: Button, isSelected: Boolean) {
        val backgroundColor = if (isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList = ContextCompat.getColorStateList(this, backgroundColor)
    }
}