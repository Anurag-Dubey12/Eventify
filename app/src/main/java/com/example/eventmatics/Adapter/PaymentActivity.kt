package com.example.eventmatics.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo

class PaymentActivity(private val paymentList: List<Paymentinfo>) :
    RecyclerView.Adapter<PaymentActivity.PaymentInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.budgetpaymentload, parent, false)
        return PaymentInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentInfoViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    inner class PaymentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val paymentNameTextView: TextView = itemView.findViewById(R.id.budgetPaymentName)
        private val paymentAmountTextView: TextView = itemView.findViewById(R.id.budgetpaymentamount)
        private val paymentDateTextView: TextView = itemView.findViewById(R.id.budgetpaymentdate)
        private val paidRadioButton: RadioButton = itemView.findViewById(R.id.Radiopaid)

        fun bind(payment: Paymentinfo) {
            paymentNameTextView.text = payment.name
            paymentAmountTextView.text = payment.amount.toString()
            paymentDateTextView.text = payment.date
        }
    }
}
