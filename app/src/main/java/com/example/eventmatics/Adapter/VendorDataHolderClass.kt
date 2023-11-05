package com.example.eventmatics.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.VendorEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VendorDataHolderClass(private val context: Context,private var vendorList:MutableList<VendorEntity>
,private val OnItemClickListener:onItemClickListener): RecyclerView.Adapter<VendorDataHolderClass.ViewHolder>() {
    private var filteredList: MutableList<VendorEntity> = mutableListOf()
    init { filteredList.addAll(vendorList) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view=LayoutInflater.from(parent.context).inflate(R.layout.vendordataholder,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int { return vendorList.size }
    fun setdata(newlist:MutableList<VendorEntity>){
        vendorList=newlist
        GlobalScope.launch(Dispatchers.IO){
        notifyDataSetChanged()
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=vendorList[position]
        holder.itemView.setOnClickListener { OnItemClickListener.onItemclick(item) }
        return holder.bind(item,position)
    }
interface onItemClickListener{ fun onItemclick(vendor: VendorEntity) }
    inner  class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val vendorNameTextView: TextView = itemView.findViewById(R.id.Vendor_Name)
        val budgetCategoryTextView: TextView = itemView.findViewById(R.id.Budget_Category)
        val vendorPhoneTextView: TextView = itemView.findViewById(R.id.VendorPhone)
        val vendorWebsiteTextView: TextView = itemView.findViewById(R.id.VendorWebsite)
        val totalAmountTextView: TextView = itemView.findViewById(R.id.text_total_amount)
        val vendorNoteTextView: TextView = itemView.findViewById(R.id.Vendor_Note)
        val vendorEmailTextView: TextView = itemView.findViewById(R.id.Vendoremail)
        val vendorAddressTextView: TextView = itemView.findViewById(R.id.VendorAddress)
        val VendorPaid: TextView = itemView.findViewById(R.id.VendorPaid)
        val cardview: CardView = itemView.findViewById(R.id.vendorcardview)!!
        fun bind(vendor: VendorEntity, position: Int){
            vendorNameTextView.text=vendor.name
            budgetCategoryTextView.text=vendor.category
            vendorPhoneTextView.text=vendor.phoneNumber
            totalAmountTextView.text=vendor.estimatedAmount
            vendorWebsiteTextView.text=vendor.website
            budgetCategoryTextView.text=vendor.category
            vendorNoteTextView.text=vendor.note
            vendorEmailTextView.text=vendor.emailid
            vendorAddressTextView.text=vendor.address
        } }
            }
