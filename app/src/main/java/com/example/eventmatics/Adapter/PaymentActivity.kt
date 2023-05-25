package com.example.eventmatics.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo

// Adapter class for the RecyclerView
class PaymentActivity(private val paymentList: List<Paymentinfo>) :
    RecyclerView.Adapter<PaymentActivity.PaymentInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_info, parent, false)
        return PaymentInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentInfoViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }
    // ViewHolder class for the items in the RecyclerView

    inner class PaymentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views in the item layout
        private val paymentNameTextView: TextView = itemView.findViewById(R.id.budgetPaymentName)
        private val paymentAmountTextView: TextView = itemView.findViewById(R.id.budgetpaymentamount)
        private val paymentDateTextView: TextView = itemView.findViewById(R.id.budgetpaymentdate)
        private val paidRadioButton: RadioButton = itemView.findViewById(R.id.Radiopaid)
        // Bind data to views
        fun bind(payment: Paymentinfo) {
            paymentNameTextView.text = payment.name
            paymentAmountTextView.text = payment.amount.toString()
            paymentDateTextView.text = payment.date
        }
    }
}
