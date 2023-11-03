package com.example.eventmatics.Login_Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.AuthenticationUid
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class signin_account : AppCompatActivity() {
    private lateinit var alreadyEmail: EditText
    private lateinit var alreadyPassfield: EditText
    private lateinit var loginButton: Button
    private lateinit var forgetpas: TextView
    private lateinit var createacc: TextView
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var rProgLayout: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_account)

        alreadyEmail = findViewById(R.id.alreadyemail)
        alreadyPassfield = findViewById(R.id.alreadypassfield)
        createacc = findViewById(R.id.createaccount)
        loginButton = findViewById(R.id.login_button)
        rProgLayout = findViewById(R.id.progressBar)
        forgetpas = findViewById(R.id.forgetpass)
        firebaseauth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        rProgLayout.visibility=View.GONE
        loginButton.setOnClickListener {
            val email = alreadyEmail.text.toString()
            val password = alreadyPassfield.text.toString()

            if (email.isEmpty()) { alreadyEmail.error = "Please Enter the Email" }
            if (password.isEmpty()) { alreadyPassfield.error = "Please Enter the Password" }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressDialog.setMessage("Logging in...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                firebaseauth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task->
                    if (task.isSuccessful) {
                       val firebaseuser=firebaseauth.currentUser
                        val uid=firebaseuser?.uid
                        AuthenticationUid.saveUserUid(this, uid.toString())
                        rProgLayout.visibility = View.VISIBLE
                        Intent(this, MainActivity::class.java).also { intent -> startActivity(intent) }
                        Toast.makeText(this, "Welcome User!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        rProgLayout.visibility = View.GONE
                        Toast.makeText(this, "Failed To Create", Toast.LENGTH_SHORT).show()
                    }
                    progressDialog.dismiss() } }
            forgetpas.setOnClickListener {
                resetpassword()
            }
        }

        createacc.setOnClickListener { Intent(this, account_creat::class.java).also { startActivity(it) } }
        checksignin()
    }
    private fun checksignin() {
        val loggoogleaccount = GoogleSignIn.getLastSignedInAccount(this)
        if (loggoogleaccount != null) { Intent(this, MainActivity::class.java).also { startActivity(it) } }
        val currentuser = FirebaseAuth.getInstance().currentUser
        if (currentuser != null) { Intent(this, MainActivity::class.java).also { startActivity(it)
            finish()
            } } }

    private fun resetpassword() {
        val email = alreadyEmail.text.toString()
        if (email.isEmpty()) { alreadyEmail.error = "Please enter your mail to reset" }
        else {
            progressDialog.setMessage("Sending Mail")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
            firebaseauth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this, "Email has been sent to your mail", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Enter A Valid Email ${e.message}", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            } } } }
