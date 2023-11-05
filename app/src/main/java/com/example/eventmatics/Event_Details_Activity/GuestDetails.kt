package com.example.eventmatics.Event_Details_Activity

import android.app.Activity
import android.content.ContentResolver
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
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.GuestEntity
import com.example.eventmatics.RoomDatabase.RoomDatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GuestDetails : AppCompatActivity() {
    private lateinit var guestNameEt: EditText
    private lateinit var guestNoteEt: EditText
    private lateinit var TotalFamilyMember: EditText
    private lateinit var invitationSentButton:  Button
    private lateinit var Pending:  Button
    private lateinit var Acccepted:  Button
    private lateinit var Denied:  Button
    private lateinit var notSentButton:  Button
    private lateinit var contactviewtv: ImageView
    private lateinit var guestPhoneEt: EditText
    private lateinit var guestPhonetv: TextView
    private lateinit var GuestAddressstv: TextView
    private lateinit var guestAddresssEt: EditText
    private var InvitationStatus:String=" "
    private var updateInvitationStatus:String=" "
    private var isInvitationSent: Boolean = false
    private var isButtonClicked:Boolean=false
    private var isAccceptanceButtonClicked:Boolean=false
    private var isAccceptanceButtonClick:Boolean=false
    private var isPendinfButtonClicked:Boolean=false
    private var isDeniedButtonClicked:Boolean=false
    private var  isAccepted:Boolean=false
    private var  isPending:Boolean=false
    private var  isDenied:Boolean=false
    private var AccceptanceStatus=""
    private var UpdatedAccceptanceStatus=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_details)
        val toolbar: Toolbar =findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        guestNameEt = findViewById(R.id.GuestNameEt)
        contactviewtv=findViewById(R.id.contactviewtv)
        guestNoteEt =findViewById(R.id.GuestNoteET)
        GuestAddressstv =findViewById(R.id.GuestAddressstv)
        TotalFamilyMember =findViewById(R.id.TotalFamilyMember)
        invitationSentButton = findViewById(R.id.Invitaionsentbut)
        Pending = findViewById(R.id.Pending)
        Denied = findViewById(R.id.Denied)
        Acccepted = findViewById(R.id.Accepted)
        notSentButton = findViewById(R.id.NotSentBut)
        guestPhoneEt = findViewById(R.id.GuestPhoneEt)
        guestPhonetv=findViewById(R.id.GuestPhonetv)
        guestAddresssEt=findViewById(R.id.GuestAddresssEt)

        Acccepted.setOnClickListener {
            isAccepted=true
            isAccceptanceButtonClicked=true
            isAccceptanceButtonClick=true
            setButtonBackground(Acccepted,true)
            setButtonBackground(Pending,false)
            setButtonBackground(Denied,false)
        }
        Pending.setOnClickListener {
            isPending=true
            isPendinfButtonClicked=true
            isAccceptanceButtonClick=true
            setButtonBackground(Acccepted,false)
            setButtonBackground(Pending,true)
            setButtonBackground(Denied,false)
        }
        Denied.setOnClickListener {
            isDenied=false
            isDeniedButtonClicked=true
            isAccceptanceButtonClick=true
            setButtonBackground(Acccepted,false)
            setButtonBackground(Pending,false)
            setButtonBackground(Denied,true)
        }
        invitationSentButton.setOnClickListener {
            InvitationStatus="Invitation Sent"
            isInvitationSent = true
            isButtonClicked=true
            setButtonBackground(invitationSentButton,true)
            setButtonBackground(notSentButton,false)
        }
        notSentButton.setOnClickListener {
            InvitationStatus="Not Sent"
            isInvitationSent = false
            isButtonClicked=true
            setButtonBackground(invitationSentButton,false)
            setButtonBackground(notSentButton,true)
        }
        contactviewtv.setOnClickListener { guestinfoview() }
        val selectedlist: GuestEntity?=intent.getParcelableExtra("selected_list")
        if(selectedlist!=null){
            guestNameEt.setText(selectedlist.name)
            TotalFamilyMember.setText(selectedlist.totalFamilyMembers)
            guestNoteEt.setText(selectedlist.note)
            guestPhoneEt.setText(selectedlist.phoneNumber)
            guestAddresssEt.setText(selectedlist.address)

            when (selectedlist.Acceptance) {
                "Pending" -> {
                    setButtonBackground(Acccepted, false)
                    setButtonBackground(Pending, true)
                    setButtonBackground(Denied, false)
                }
                "Accepted" -> {
                    setButtonBackground(Acccepted, true)
                    setButtonBackground(Pending, false)
                    setButtonBackground(Denied, false)
                }
                "Denied" -> {
                    setButtonBackground(Acccepted, false)
                    setButtonBackground(Pending, false)
                    setButtonBackground(Denied, true)
                }
                else -> {
                }
            }
            if(selectedlist.isInvitationSent=="Invitation Sent"){
                setButtonBackground(invitationSentButton,true)
                setButtonBackground(notSentButton,false)
            }
            if(selectedlist.isInvitationSent=="Not Sent"){
                setButtonBackground(invitationSentButton,false)
                setButtonBackground(notSentButton,true)
            } } }

    private fun guestinfoview() {
        val isVisible= guestPhonetv.visibility==View.VISIBLE
        val visibility=if(isVisible) View.GONE else View.VISIBLE
        val drawableimage=if(isVisible) R.drawable.drop_arrow else R.drawable.uparrow

        val componetview= listOf(guestPhonetv,guestAddresssEt, guestPhoneEt,guestAddresssEt, GuestAddressstv)
        componetview.forEach { it.visibility = visibility }
        contactviewtv.setImageResource(drawableimage)
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
                val selectedlist: GuestEntity?=intent.getParcelableExtra("selected_list")
                if(selectedlist!=null){ updateDatabase(selectedlist.id) }
                else{ AddValueToDatabase() }
                true
            }
        else->super.onOptionsItemSelected(item)
    } }
    private fun AddValueToDatabase() {
        GlobalScope.launch(Dispatchers.IO){
        val dao = RoomDatabaseManager.getEventsDao(applicationContext)
        val guestName = guestNameEt.text.toString()
        val guestNote = guestNoteEt.text.toString()
        val totalFamilyMembers = TotalFamilyMember.text.toString()
        val guestPhone = guestPhoneEt.text.toString()
        val guestAddress = guestAddresssEt.text.toString()
        if(isAccepted){ AccceptanceStatus="Accepted" }
        else if (isPending){ AccceptanceStatus="Pending" }
        else if (isDenied){ AccceptanceStatus="Denied" }
        else{ AccceptanceStatus=" " }
        val GuestList = GuestEntity(1, guestName, totalFamilyMembers, guestNote, InvitationStatus,
            guestPhone, AccceptanceStatus,guestAddress)
        dao.InsertGuest(GuestList)
        finish()
        }
    }
    private fun updateDatabase(id: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val dao = RoomDatabaseManager.getEventsDao(applicationContext)
            val guestName = guestNameEt.text.toString()
            val guestNote = guestNoteEt.text.toString()
            val totalFamilyMembers = TotalFamilyMember.text.toString()
            val guestPhone = guestPhoneEt.text.toString()
            val guestAddress = guestAddresssEt.text.toString()

            // Retrieve the previous Invitation Status and Acceptance Status
            val selectedList: GuestEntity? = intent.getParcelableExtra("selected_list")
            val previousInvitationStatus = selectedList?.isInvitationSent
            val previousAcceptanceStatus = selectedList?.Acceptance

            val updatedInvitationStatus = if (isInvitationSent) "Invitation Sent" else "Not Sent"
            val updatedAcceptanceStatus = when {
                isAccceptanceButtonClicked -> "Accepted"
                isPendinfButtonClicked -> "Pending"
                isDeniedButtonClicked -> "Denied"
                else -> previousAcceptanceStatus.toString() // Use previous status if none of the buttons were clicked
            }

            val guestEntity = GuestEntity(id, guestName, totalFamilyMembers, guestNote, updatedInvitationStatus, guestPhone, updatedAcceptanceStatus, guestAddress)

            dao.updateGuest(guestEntity)

            runOnUiThread {
                finish()
            }
        }
    }

    //retriveing the contact name and number form the device
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 900) {
            val contactUri = data?.data ?: return
            val contactInfo = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER)
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
            } } }
    fun setButtonBackground(button: Button, isSelected: Boolean) {
        val backgroundColor = if (isSelected) R.color.light_blue else R.color.Light_Lemon
        button.backgroundTintList = ContextCompat.getColorStateList(this, backgroundColor)
    }
}