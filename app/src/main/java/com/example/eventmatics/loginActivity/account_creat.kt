@file:Suppress("DEPRECATION")

package com.example.eventmatics.loginActivity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.eventmatics.MainActivity
import com.example.eventmatics.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class account_creat : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
    private lateinit var forgetpas:TextView
    private lateinit var createButton: Button
    private lateinit var googleLoginButton: Button
    private lateinit var alreadyHaveAccountText: TextView
    private lateinit var firebaseauth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firestore : FirebaseFirestore
    val RC_SIGN_IN = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_creation)
        emailField = findViewById(R.id.emailfield)
        passField = findViewById(R.id.passfield)
        forgetpas = findViewById(R.id.forgetpass)
        createButton = findViewById(R.id.createbutton)
        googleLoginButton = findViewById(R.id.google_creation)
        alreadyHaveAccountText = findViewById(R.id.alreadyfield)
        progressDialog=ProgressDialog(this)
        firebaseauth=FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        createButton.setOnClickListener {
            val email=emailField.text.toString()
            val password=passField.text.toString()

            if(email.isEmpty()){
                emailField.error="Please Enter the Email"
            }
            if( password.isEmpty()){
                passField.error="Please Enter the Password"
            }
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseauth.createUserWithEmailAndPassword(email,password).addOnSuccessListener{
                    firestore.collection("User")
                        .document(FirebaseAuth.getInstance().uid.toString())
                        .set(UserDetails(email,password))
                    Intent(this,MainActivity::class.java).also {
                            startActivity(it)
                        Toast.makeText(this, "Logging Successfully!", Toast.LENGTH_SHORT).show()
                        progressDialog.cancel()
                    }
                }.addOnFailureListener {e->
                    Toast.makeText(this,"Email not found +${e.message}",Toast.LENGTH_SHORT).show()
                    progressDialog.cancel()
                }
                progressDialog.setMessage("Logging ...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()
                }
            }

        googleLoginButton.setOnClickListener {
            googleaccess()
        }
        forgetpas.setOnClickListener {
            resetpassword()

        }
        alreadyHaveAccountText.setOnClickListener {
            Intent(this,signin_account::class.java).also {
                startActivity(it)
        }

    }
}

    private fun googleaccess() {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient=GoogleSignIn.getClient(this,gso)
        val signinintent=googleSignInClient.signInIntent
        startActivityForResult(signinintent,RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGN_IN){
            val accounttask=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=accounttask.getResult(ApiException::class.java)
                handlesigninresult(account)
            }
            catch (e:Exception){
                Toast.makeText(this,"Google Sing in Failed ${e.message}",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun handlesigninresult(account: GoogleSignInAccount?) {
        Log.d(TAG,"FirebaseAuthWithGoogle ${account!!.id}" )
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseauth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful){
                    val user=firebaseauth.currentUser
                    addusertofirestore(user!!)
                }
                else{
                    Log.w(TAG, "SignInWithCredential:Failure", it.exception)

                }
            }
    }

    private fun addusertofirestore(user:FirebaseUser ) {
        val Usermap = hashMapOf(
            "name" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl
        )
//        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("GoogleSignINUser")
            .document(user.uid)
            .set(Usermap)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
            .addOnFailureListener {e->
                Toast.makeText(this,"Check Internet Connectivity ${e.message}",Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
        progressDialog.setMessage("Logging...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

    }

    private fun resetpassword() {
        val email=emailField.text.toString()
        if(email.isEmpty()){
            emailField.error="Please enter your mail to reset"
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