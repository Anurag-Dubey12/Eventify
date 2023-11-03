package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Paymentinfo

class PaymentActivityAdapter(private val context: Context, private val paymentList: MutableList<Paymentinfo>
,private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PaymentActivityAdapter.PaymentInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_info, parent, false)
        return PaymentInfoViewHolder(view)
    }
    interface OnItemClickListener{ fun onItemClick(payment: Paymentinfo) }
    override fun onBindViewHolder(holder: PaymentInfoViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
        holder.EditPayment.setOnClickListener { itemClickListener.onItemClick(payment) }
    }

    override fun getItemCount(): Int { return paymentList.size }
    inner class PaymentInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val paymentNameTextView: TextView = itemView.findViewById(R.id.budgetPaymentName)
        private val paymentAmountTextView: TextView = itemView.findViewById(R.id.budgetpaymentamount)
        private val paymentDateTextView: TextView = itemView.findViewById(R.id.budgetpaymentdate)
        private val budgetpaymentstatus: TextView = itemView.findViewById(R.id.budgetpaymentstatus)
        val EditPayment: ImageView = itemView.findViewById(R.id.EditPayment)

        fun bind(payment: Paymentinfo) {
            paymentNameTextView.text = payment.name
            budgetpaymentstatus.text = payment.status
            paymentAmountTextView.text = payment.amount.toString()
            paymentDateTextView.text = payment.date
        } } }
