package com.example.eventmatics.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter.LocalDatabase
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Vendor

class VendorDataHolderClass(private val context: Context,private var vendorList:MutableList<Vendor>
,private val OnItemClickListener:onItemClickListener): RecyclerView.Adapter<VendorDataHolderClass.ViewHolder>() {

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
    fun setdata(newlist:MutableList<Vendor>){
        vendorList=newlist
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=vendorList[position]
        holder.itemView.setOnClickListener {
            OnItemClickListener.onItemclick(item)
        }
        return holder.bind(item,position)
    }
interface onItemClickListener{
    fun onItemclick(vendor: Vendor)
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
        val VendorPaid: TextView = itemView.findViewById(R.id.VendorPaid)
        val cardview: CardView = itemView.findViewById(R.id.vendorcardview)!!
        fun bind(vendor: Vendor, position: Int){
            vendorNameTextView.text=vendor.name
            budgetCategoryTextView.text=vendor.category
            vendorPhoneTextView.text=vendor.phonenumber
            totalAmountTextView.text=vendor.balance
            vendorWebsiteTextView.text=vendor.website
            budgetCategoryTextView.text=vendor.category
            vendorNoteTextView.text=vendor.note
            vendorEmailTextView.text=vendor.emailid
            vendorAddressTextView.text=vendor.address
            val databasename = getSharedPreference(context, "databasename").toString()
            val db = LocalDatabase(context, databasename)
            val isVendorPaid=db.isVendorPaid(vendor.id)
            if(isVendorPaid){
                VendorPaid.text="Paid"
                VendorPaid.setTextColor(Color.parseColor("#00FF00"))
                cardview.setBackgroundColor(Color.parseColor("#F5F5F5"))
            }
            else{
                VendorPaid.text="Not Paid"
            }
        }
        fun getSharedPreference(context: Context, key: String): String? {
            val sharedPref = context.getSharedPreferences("Database", Context.MODE_PRIVATE)
            return sharedPref.getString(key, null)
        }
    }
}