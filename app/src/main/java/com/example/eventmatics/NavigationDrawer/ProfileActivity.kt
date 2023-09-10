package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.eventmatics.Login_Activity.ProfileInfo
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfile
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfileDatabase
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URI

class ProfileActivity : AppCompatActivity() {
    private lateinit var UserName: TextView
    private lateinit var UserProfile: ImageView
//    private lateinit var UserEmail: TextView
    private lateinit var ProfileDialog: BottomSheetDialog
    lateinit var UserDetails: SharedPreferences
    private val CAPTURE_REQ_CODE = 101
    private val GALLERY_REQ_CODE = 201

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        UserName=findViewById(R.id.UserName)
        UserProfile=findViewById(R.id.profileImage)
        UserDetails = this.getSharedPreferences("UserDetails", MODE_PRIVATE)

        val db=UserProfileDatabase(this)
        val userid=FirebaseAuth.getInstance().currentUser?.uid.toString()
        val User=db.getUserProfilebyID(1)
        UserName.text=User?.name
        val UserImage=User?.Image
        if(UserImage!=null){
            val ImageBitmap=BitmapFactory.decodeByteArray(UserImage,0,UserImage.size)
            UserProfile.setImageBitmap(ImageBitmap)
        }

        UserProfile.setOnClickListener {
            ProfileUpload()
        }
    }

    fun ProfileUpload() {
        ProfileDialog = BottomSheetDialog(this)
        ProfileDialog.setContentView(R.layout.profiledialog)
        ProfileDialog.show()

        val capturebtn = ProfileDialog.findViewById<ImageView>(R.id.ImageCapture)
        val gallerybtn = ProfileDialog.findViewById<ImageView>(R.id.ImageGallery)

        capturebtn?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAPTURE_REQ_CODE)
            } else {
                val capture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(capture, CAPTURE_REQ_CODE)
                ProfileDialog.dismiss()
            }
        }

        gallerybtn?.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, GALLERY_REQ_CODE)
            ProfileDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val db=UserProfileDatabase(this)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAPTURE_REQ_CODE -> {

                }
                GALLERY_REQ_CODE -> {
                    val selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                    val ImgeByte=GetImageByte(selectedImageUri)

                        val userid=FirebaseAuth.getInstance().currentUser?.uid
                        if(userid!=null){
                            val userprofile=UserProfile(1," Anurag",ImgeByte)
                            db.insertUserProfile(userprofile)
                            Log.d("User_Info","Id:${userprofile.id} \n User name:${userprofile.name}\n" +
                                    "User Image Bytes:${userprofile.Image} \n userid:$userid")
                            val imageBitmap = BitmapFactory.decodeByteArray(ImgeByte, 0, ImgeByte.size)
                            UserProfile.setImageBitmap(imageBitmap)

                            Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    fun GetImageByte(ImageURI:Uri):ByteArray{
        val inputStream=contentResolver.openInputStream(ImageURI)
        return inputStream?.readBytes() ?: ByteArray(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
