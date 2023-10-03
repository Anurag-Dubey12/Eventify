
package com.example.eventmatics.Login_Activity

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
import com.example.eventmatics.SQLiteDatabase.Dataclass.AuthenticationUid
import com.example.eventmatics.getSharedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class account_creat : AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var passField: EditText
    private lateinit var createButton: MaterialButton
    private lateinit var googleLoginButton: MaterialButton
    private lateinit var alreadyHaveAccountText: TextView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firestore: FirebaseFirestore
    private val RC_SIGN_IN = 1
    private val TAG = "account_creat"
    lateinit var  firebaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_creation)
        emailField = findViewById(R.id.emailfield)
        passField = findViewById(R.id.passfield)
        createButton = findViewById(R.id.createbutton)
        googleLoginButton = findViewById(R.id.google_creation)
        alreadyHaveAccountText = findViewById(R.id.alreadyfield)
        progressDialog = ProgressDialog(this)
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        createButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passField.text.toString()

            if (email.isEmpty()) {
                emailField.error = "Please Enter the Email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passField.error = "Please Enter the Password"
                return@setOnClickListener
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val firebaseUser = authResult.user
                    val userUid = firebaseUser?.uid

                    if (userUid != null) {
                        AuthenticationUid.saveUserUid(this,userUid)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Logging Successfully!", Toast.LENGTH_SHORT).show()
                        progressDialog.cancel()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                    progressDialog.cancel()
                }

            progressDialog.setMessage("Logging ...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.show()
        }

        googleLoginButton.setOnClickListener {
            googleSignIn()
        }

        alreadyHaveAccountText.setOnClickListener {
            Intent(this, signin_account::class.java).also {
                startActivity(it)
            }
        }
    }


    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                handleSignInResult(account)
            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In Failed: ${e.message}")
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignInResult(account: GoogleSignInAccount?) {
        Log.d(TAG, "FirebaseAuthWithGoogle ${account?.id}")

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    addUserToFirestore(user)
                } else {
                    Log.w(TAG, "SignInWithCredential: Failure", task.exception)
                    Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToFirestore(user: FirebaseUser?) {
        if (user == null) {
            Toast.makeText(this, "User is null", Toast.LENGTH_SHORT).show()
            return
        }

        val userMap = hashMapOf(
            "name" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl.toString()
        )

        firestore.collection("UserProfile")
            .document(user.uid)
            .set(userMap)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Log in successful", Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding user to Firestore: ${e.message}")
                Toast.makeText(this, "Error adding user to Firestore", Toast.LENGTH_SHORT).show()
                progressDialog.cancel()
            }

        progressDialog.setMessage("Logging...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }
}