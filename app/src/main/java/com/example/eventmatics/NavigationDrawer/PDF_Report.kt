package com.example.eventmatics.NavigationDrawer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.Adapter.PDF_HolderAdapter
import com.example.eventmatics.R
import com.example.eventmatics.data_class.PdfFileItem
import java.io.File

class PDF_Report : AppCompatActivity() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var pdfListAdapter: PDF_HolderAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_report)
        recyclerView=findViewById(R.id.recyclerview)
        val pdfDirectory = File(Environment.getExternalStorageDirectory(), "Eventify")
        val pdfFiles = getAllPdfFilesFromDirectory(pdfDirectory)

        pdfListAdapter = PDF_HolderAdapter(pdfFiles)
        recyclerView.adapter = pdfListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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


}

