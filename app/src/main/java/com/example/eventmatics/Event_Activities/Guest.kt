package com.example.eventmatics.Event_Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R

class Guest : AppCompatActivity() {
    private lateinit var guestNameEt: EditText
    private lateinit var maleButton: AppCompatButton
    private lateinit var femaleButton: AppCompatButton
    private lateinit var adultButton: AppCompatButton
    private lateinit var childButton: AppCompatButton
    private lateinit var babyButton: AppCompatButton
    private lateinit var guestNoteEt: EditText
    private lateinit var contactviewtv: ImageView
    private lateinit var guestPhoneEt: EditText
    private lateinit var guestPhonetv: TextView
    private lateinit var guestEmailEt: EditText
    private lateinit var guestEmailtv: TextView
    private lateinit var guestAddresssEt: EditText
    private lateinit var guestAddressstv: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest)
        guestNameEt = findViewById(R.id.GuestNameEt)
        maleButton = findViewById(R.id.Malebut)
        femaleButton =findViewById(R.id.femalebut)
        adultButton = findViewById(R.id.Adultbut)
        contactviewtv=findViewById(R.id.contactviewtv)
        childButton = findViewById(R.id.ChildBut)
        babyButton = findViewById(R.id.Babybut)
        guestNoteEt =findViewById(R.id.GuestNoteET)

        guestPhoneEt = findViewById(R.id.GuestPhoneEt)
        guestEmailEt = findViewById(R.id.GuestEmailEt)
        guestAddresssEt = findViewById(R.id.GuestAddresssEt)
        guestPhonetv=findViewById(R.id.GuestPhonetv)
        guestEmailtv=findViewById(R.id.GuestEmailtv)
        guestAddressstv=findViewById(R.id.GuestAddressstv)

        contactviewtv.setOnClickListener {
            guestinfoview()
        }
        }

    private fun guestinfoview() {
        if(guestPhonetv.visibility== View.VISIBLE && guestEmailtv.visibility== View.VISIBLE && guestAddressstv.visibility== View.VISIBLE &&
            guestPhoneEt.visibility== View.VISIBLE && guestEmailEt.visibility== View.VISIBLE && guestAddresssEt.visibility== View.VISIBLE){

            guestPhonetv.visibility= View.GONE
            guestEmailtv.visibility= View.GONE
            guestAddressstv.visibility= View.GONE
            guestPhoneEt.visibility= View.GONE
            guestEmailEt.visibility= View.GONE
            guestAddresssEt.visibility= View.GONE
        }
        else{
            guestPhonetv.visibility= View.VISIBLE
            guestEmailtv.visibility= View.VISIBLE
            guestAddressstv.visibility= View.VISIBLE
            guestPhoneEt.visibility= View.VISIBLE
            guestEmailEt.visibility= View.VISIBLE
            guestAddresssEt.visibility= View.VISIBLE
        }
    }
    }