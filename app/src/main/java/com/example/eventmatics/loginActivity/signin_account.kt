package com.example.eventmatics.loginActivity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class signin_account : AppCompatActivity() {
    private lateinit var alreadyEmail: EditText
    private lateinit var alreadyPassfield: EditText
    private lateinit var loginButton: Button
    private lateinit var googleLogin: Button
    private lateinit var forgetpas: TextView
    private lateinit var createacc:TextView
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_account)
        alreadyEmail = findViewById(R.id.alreadyemail)
        alreadyPassfield = findViewById(R.id.alreadypassfield)
        createacc = findViewById(R.id.createaccount)
        loginButton = findViewById(R.id.login_button)
        googleLogin = findViewById(R.id.google_login)
        forgetpas = findViewById(R.id.forgetpass)
        firebaseauth=FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)
        loginButton.setOnClickListener {
            val email=alreadyEmail.text.toString()
            val password=alreadyPassfield.text.toString()

            if(email.isEmpty()){
                alreadyEmail.error="Please Enter the Email"
            }
            if( password.isEmpty()){
                alreadyPassfield.error="Please Enter the Password"
            }

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseauth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if(it.isSuccessful){
                        Intent(this,MainActivity::class.java).also {
                            startActivity(it)
                        }
                        Toast.makeText(this,"Welcome User!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,"Failed To Create", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            forgetpas.setOnClickListener {
                resetpassword()

            }
        }
        createacc.setOnClickListener {
            Intent(this,account_creat::class.java).also {
                startActivity(it)
            }
        }
        checksignin()

        }

    private fun checksignin() {
        val loggoogleaccount=GoogleSignIn.getLastSignedInAccount(this)
        if(loggoogleaccount!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
            }
        }
        val currentuser=FirebaseAuth.getInstance().currentUser
        if(currentuser!=null){
            Intent(this,MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

    }

    private fun resetpassword() {
        val email=alreadyEmail.text.toString()
        if(email.isEmpty()){
            alreadyEmail.error="Please enter your mail to reset"
        }
        else{
            firebaseauth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this,"Email has been sent your mail",Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }.addOnFailureListener{e->
                Toast.makeText(this,"Enter A Valid Email ${e.message}",Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
            progressDialog.setMessage("Sending Mail")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }
    }



   }
