package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.VendorPaymentinfo

class VendorPaymentActivityAdapter(private val context: Context,
                                   private val paymentList: MutableList<VendorPaymentinfo>,
                                   private val onitemclicklistener:OnItemClickListener) :
    RecyclerView.Adapter<VendorPaymentActivityAdapter.PaymentInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vendor_payment_info, parent, false)
        return PaymentInfoViewHolder(view)
    }
    interface OnItemClickListener{
        fun onitemclick(paymentList: VendorPaymentinfo)
    }


    override fun onBindViewHolder(holder: PaymentInfoViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
        holder.EditPayment.setOnClickListener {
            onitemclicklistener.onitemclick(payment)
        }
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }
    inner class PaymentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val VendorPaymentName: TextView = itemView.findViewById(R.id.VendorPaymentName)
        private val Vendorpaymentamount: TextView = itemView.findViewById(R.id.Vendorpaymentamount)
        private val Vendorpaymentdate: TextView = itemView.findViewById(R.id.Vendorpaymentdate)
        private val Vendorpaymentstatus: TextView = itemView.findViewById(R.id.Vendorpaymentstatus)
        val EditPayment: ImageView = itemView.findViewById(R.id.EditPayment)
//        private val paidRadioButton: MaterialRadioButton = itemView.findViewById(R.id.Radiopaid)
        fun bind(payment: VendorPaymentinfo) {
            VendorPaymentName.text = payment.name
            Vendorpaymentstatus.text = payment.status
            Vendorpaymentamount.text = payment.amount.toString()
            Vendorpaymentdate.text = payment.date
        }
    }
}

