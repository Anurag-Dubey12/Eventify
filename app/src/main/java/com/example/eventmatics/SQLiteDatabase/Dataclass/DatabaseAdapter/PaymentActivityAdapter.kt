package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.Paymentinfo

class PaymentActivityAdapter(private val context: Context, private val paymentList: MutableList<Paymentinfo>) :
    RecyclerView.Adapter<PaymentActivityAdapter.PaymentInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_info, parent, false)
        return PaymentInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentInfoViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
//        holder.itemView.setOnClickListener {
//            itemClickListener.onItemClick(payment)
//        }
    }
//interface OnItemClickListener{
//    fun onItemClick(payment:Paymentinfo)
//}
    override fun getItemCount(): Int {
        return paymentList.size
    }
    inner class PaymentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views in the item layout
        private val paymentNameTextView: TextView = itemView.findViewById(R.id.budgetPaymentName)
        private val paymentAmountTextView: TextView = itemView.findViewById(R.id.budgetpaymentamount)
        private val paymentDateTextView: TextView = itemView.findViewById(R.id.budgetpaymentdate)
        private val budgetpaymentstatus: TextView = itemView.findViewById(R.id.budgetpaymentstatus)
        private val budgetid: TextView = itemView.findViewById(R.id.budgetid)
        private val paidRadioButton: RadioButton = itemView.findViewById(R.id.Radiopaid)

        // Bind data to views
        fun bind(payment: Paymentinfo) {
            paymentNameTextView.text = payment.name
            budgetpaymentstatus.text = payment.status
            budgetid.text = payment.budgetid.toString()
            paymentAmountTextView.text = payment.amount.toString()
            paymentDateTextView.text = payment.date
        }
    }
}
