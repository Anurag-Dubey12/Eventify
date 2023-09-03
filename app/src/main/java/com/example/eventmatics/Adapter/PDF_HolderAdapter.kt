package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.PdfFileItem

class PDF_HolderAdapter(private val pdfFiles: List<PdfFileItem>) :
    RecyclerView.Adapter<PDF_HolderAdapter.PDFViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PDFViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pdf_view, parent, false)
        return PDFViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PDFViewHolder, position: Int) {
        val pdfFile = pdfFiles[position]
        holder.bind(pdfFile)
    }

    override fun getItemCount(): Int = pdfFiles.size

    inner class PDFViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pdfNameTextView: TextView = itemView.findViewById(R.id.pdfNameTextView)

        fun bind(pdfFile: PdfFileItem) {
            pdfNameTextView.text = pdfFile.name

            itemView.setOnClickListener {


            }
        }
    }
}
