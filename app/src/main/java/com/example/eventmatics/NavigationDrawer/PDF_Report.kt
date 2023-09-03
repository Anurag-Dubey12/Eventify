package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.PDF_HolderAdapter
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.data_class.PdfFileItem
import java.io.File

class PDF_Report : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var pdfListAdapter: PDF_HolderAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_report)
        recyclerView = findViewById(R.id.recyclerview)

        // Log the external storage directory to check the path
        val pdfDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        val childDirectory = File(pdfDirectory, "EventName")

        Log.d("PDF_Report", "pdfDirectory: ${pdfDirectory.absolutePath}")
        Log.d("PDF_Report", "childDirectory: ${childDirectory.absolutePath}")

        // Check if childDirectory exists and list files
        if (pdfDirectory.exists() && pdfDirectory.isDirectory) {
            val pdfFiles = getAllPdfFilesFromDirectory(pdfDirectory)
            pdfListAdapter = PDF_HolderAdapter(pdfFiles)

            // Log the number of items in the adapter
            Log.d("PDF_Report", "Number of items in the adapter: ${pdfListAdapter.itemCount}")

            recyclerView.adapter = pdfListAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            Log.d("PDF_Report", "childDirectory does not exist or is not a directory.")
        }
    }

    fun getSharedPreference(context: Context, key: String): String? {
        val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }

    private fun getAllPdfFilesFromDirectory(directory: File): List<PdfFileItem> {
        val pdfFiles = mutableListOf<PdfFileItem>()

        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles { _, filename -> filename.endsWith(".pdf") }
            files?.forEach { file ->
                pdfFiles.add(PdfFileItem(file.name, file.absolutePath))
            }
        }

        return pdfFiles
    }
    private fun openPdfWithExternalViewer(context: Context, pdfPath: String) {
        val pdfFile = File(pdfPath)
        val uri = Uri.fromFile(pdfFile)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        // Create a chooser to let the user select an external PDF viewer app
        val chooserIntent = Intent.createChooser(intent, "Open PDF with...")

        try {
            context.startActivity(chooserIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
