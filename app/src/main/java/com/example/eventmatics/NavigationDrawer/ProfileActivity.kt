package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eventmatics.Adapter.EventDatabaseAdapter
import com.example.eventmatics.Login_Activity.ProfileInfo
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.NamesDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfile
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.UserProfileDatabase
import com.example.eventmatics.getSharedPreference
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URI

class ProfileActivity : AppCompatActivity() {
//    private lateinit var databaseRecycler: RecyclerView
//    private lateinit var adapter: EventDatabaseAdapter
//    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
//        databaseRecycler = findViewById(R.id.DatabaseRecycler)
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
//        swipeRefreshLayout.setOnRefreshListener {
//            Handler().postDelayed({
//                val databasename = getSharedPreference(this, "databasename").toString()
//                val Databasenames = NamesDatabase(this)
//                val DatabaseNames=Databasenames.getAllEventNames()
//                adapter= EventDatabaseAdapter(this,DatabaseNames){position ->
//
//                }
//                databaseRecycler.adapter=adapter
//                databaseRecycler.layoutManager= LinearLayoutManager(this)
//                adapter.notifyDataSetChanged()
//                swipeRefreshLayout.isRefreshing=false
//            },3000)
//        }
////        EventNameInflate()
//        showEventData()
//    }
//
//    fun showEventData() {
//        val databasename = getSharedPreference(this, "databasename").toString()
//        val Databasenames = NamesDatabase(this)
//        val DatabaseNames=Databasenames.getAllEventNames()
//        adapter= EventDatabaseAdapter(this,DatabaseNames){EventName ->
//
//        }
//        databaseRecycler.adapter=adapter
//        databaseRecycler.layoutManager= LinearLayoutManager(this)
//        adapter.notifyDataSetChanged()
//
//    }
//    fun getsharedpreference(context: Context, key:String):String?{
//        val sharedvalue=context.getSharedPreferences("Database", Context.MODE_PRIVATE)
//        return sharedvalue.getString(key,null)
//    }
}}
