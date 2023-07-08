package com.example.eventmatics.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Vendor

class VendorDataHolderClass(private val vendorList:List<Vendor>): RecyclerView.Adapter<VendorDataHolderClass.ViewHolder>() {

    private var filteredList: MutableList<Vendor> = mutableListOf()

    init {
        filteredList.addAll(vendorList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.vendordataholder,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return vendorList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=vendorList[position]
        return holder.bind(item)
    }
    fun updateList(filteredList: MutableList<Vendor>) {
        this.filteredList.clear()
        this.filteredList.addAll(filteredList)
        notifyDataSetChanged()
    }
    inner  class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        private val VendorName: TextView = itemView.findViewById(R.id.vendorname)
        private val total_amount: TextView = itemView.findViewById(R.id.text_total_amount)
        fun bind(vendor:Vendor){
            VendorName.text=vendor.name
            total_amount.text=vendor.estimatedAmount
        }

    }
}