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
        val vendorNameTextView: TextView = itemView.findViewById(R.id.Vendor_Name)
        val budgetCategoryTextView: TextView = itemView.findViewById(R.id.Budget_Category)
        val vendorPhoneTextView: TextView = itemView.findViewById(R.id.VendorPhone)
        val vendorWebsiteTextView: TextView = itemView.findViewById(R.id.VendorWebsite)
        val totalAmountTextView: TextView = itemView.findViewById(R.id.text_total_amount)
        val vendorNoteTextView: TextView = itemView.findViewById(R.id.Vendor_Note)
        val vendorEmailTextView: TextView = itemView.findViewById(R.id.Vendoremail)
        val vendorAddressTextView: TextView = itemView.findViewById(R.id.VendorAddress)
        fun bind(vendor:Vendor){
            vendorNameTextView.text=vendor.name
            budgetCategoryTextView.text=vendor.category
            vendorPhoneTextView.text=vendor.phonenumber
            totalAmountTextView.text=vendor.balance
            vendorWebsiteTextView.text=vendor.website
            budgetCategoryTextView.text=vendor.category
            vendorNoteTextView.text=vendor.note
            vendorEmailTextView.text=vendor.emailid
            vendorAddressTextView.text=vendor.address
        }

    }
}