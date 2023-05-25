package com.example.eventmatics.Event_Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R

class Guest : AppCompatActivity() {
    private lateinit var comguestNameEt: EditText
    private lateinit var commaleButton: AppCompatButton
    private lateinit var comfemaleButton: AppCompatButton
    private lateinit var comadultButton: AppCompatButton
    private lateinit var comchildButton: AppCompatButton
    private lateinit var combabyButton: AppCompatButton
    private lateinit var comguestNoteEt: EditText
    private lateinit var comcontactviewtv: ImageView
    private lateinit var comcontacttake: ImageView
    private lateinit var comguestPhoneEt: EditText
    private lateinit var comguestPhonetv: TextView
    private lateinit var comguestEmailEt: EditText
    private lateinit var comguestEmailtv: TextView
    private lateinit var comguestAddresssEt: EditText
    private lateinit var comguestAddressstv: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest)
        comguestNameEt = findViewById(R.id.comGuestNameEt)
        commaleButton = findViewById(R.id.comMalebut)
        comfemaleButton =findViewById(R.id.comfemalebut)
        comadultButton = findViewById(R.id.comAdultbut)
        comcontactviewtv=findViewById(R.id.contactviewtv)
        comcontacttake=findViewById(R.id.contactview)
        comchildButton = findViewById(R.id.comChildBut)
        combabyButton = findViewById(R.id.comBabybut)
        comguestNoteEt =findViewById(R.id.comGuestNoteET)

        comguestPhoneEt = findViewById(R.id.comGuestPhoneEt)
        comguestEmailEt = findViewById(R.id.comGuestEmailEt)
        comguestAddresssEt = findViewById(R.id.comGuestAddresssEt)
        comguestPhonetv=findViewById(R.id.comGuestPhonetv)
        comguestEmailtv=findViewById(R.id.comGuestEmailtv)
        comguestAddressstv=findViewById(R.id.comGuestAddressstv)

        comcontactviewtv.setOnClickListener {
            guestinfoview()
        }
        comcontacttake.setOnClickListener {
            takecontactinfo()
        }
        }
    private fun takecontactinfo() {
    Intent(Intent.ACTION_PICK).also {
        it.type=ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(it,901)
    }

    }


    private fun guestinfoview() {
        if(comguestPhonetv.visibility== View.VISIBLE && comguestEmailtv.visibility== View.VISIBLE && comguestAddressstv.visibility== View.VISIBLE &&
            comguestPhoneEt.visibility== View.VISIBLE && comguestEmailEt.visibility== View.VISIBLE && comguestAddresssEt.visibility== View.VISIBLE){

            comguestPhonetv.visibility= View.GONE
            comguestEmailtv.visibility= View.GONE
            comguestAddressstv.visibility= View.GONE
            comguestPhoneEt.visibility= View.GONE
            comguestEmailEt.visibility= View.GONE
            comguestAddresssEt.visibility= View.GONE
        }
        else{
            comguestPhonetv.visibility= View.VISIBLE
            comguestEmailtv.visibility= View.VISIBLE
            comguestAddressstv.visibility= View.VISIBLE
            comguestPhoneEt.visibility= View.VISIBLE
            comguestEmailEt.visibility= View.VISIBLE
            comguestAddresssEt.visibility= View.VISIBLE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 901) {
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

                    comguestNameEt.setText(contactName)
                    comguestPhoneEt.setText(contactNumber)
                }
                it.close()
            }
        }
    }


}