package com.example.eventmatics.Login_Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class Login_SignUp_Option : AppCompatActivity() {
    private lateinit var SignUp:MaterialButton
    private lateinit var LogIn:MaterialButton
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sign_up_option)
        SignUp=findViewById(R.id.SignUp)
        LogIn=findViewById(R.id.LogIn)
        checksignin()
        SignUp.setOnClickListener { Intent(this,signin_account::class.java).also { startActivity(it) } }
        LogIn.setOnClickListener { Intent(this,account_creat::class.java).also { startActivity(it) } }

    }
    private fun checksignin() {
        val loggoogleaccount = GoogleSignIn.getLastSignedInAccount(this)
        if (loggoogleaccount != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }
        val currentuser = FirebaseAuth.getInstance().currentUser
        if (currentuser != null) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }
}