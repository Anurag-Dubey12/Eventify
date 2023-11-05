package com.example.eventmatics.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.eventmatics.R
import com.example.eventmatics.RoomDatabase.DataClas.VendorPaymentEntity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class VendorFragment(private val context: Context,private var fragmentManager:FragmentManager,
                     private val VendorId:Long?,
    val paymentinfo: VendorPaymentEntity?) : BottomSheetDialogFragment() {
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var vendorButtonPending: AppCompatButton
    private lateinit var vendorButtonPaid: AppCompatButton
    private lateinit var vendorexpireDate: TextView
    private lateinit var buttonSubmit: MaterialButton
    private var isPaid:Boolean=false
    private var isButtonClicked:Boolean=false
    var paymentStatus :String=" "
    var updatepaymentStatus :String=" "

    interface UserDataListener {
        fun onUserDataEntered(userData: VendorPaymentEntity)
    }
    interface UserDataUpdateListener{
        fun onuserupdate(userData: VendorPaymentEntity)
    }
    private var userupdatelistener:UserDataUpdateListener?=null

    private var userDataListener: UserDataListener? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_vendor, container, false)
        return view
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName = view.findViewById(R.id.editTextName)
        editTextAmount = view.findViewById(R.id.editTextAmount)
        vendorButtonPending = view.findViewById(R.id.vendorbuttonPending)
        vendorButtonPaid = view.findViewById(R.id.vendorbuttonPaid)
        vendorexpireDate = view.findViewById(R.id.editTextDate)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        vendorButtonPending.setOnClickListener {
            isPaid=false
            isButtonClicked=true
            setButtonBackground(vendorButtonPending,true)
            setButtonBackground(vendorButtonPaid,false)
        }

        vendorButtonPaid.setOnClickListener {
            isButtonClicked=true
            isPaid=true
            setButtonBackground(vendorButtonPaid,true)
            setButtonBackground(vendorButtonPending,false)
        }
        vendorexpireDate.setOnClickListener { showDatePicker() }
        buttonSubmit.setOnClickListener {
            val Payment: VendorPaymentEntity?=arguments?.getParcelable("VendorPayment")
            if(Payment!=null){
                val name=editTextName.text.toString()
                val amount=editTextAmount.text.toString().toFloat()
                val date=vendorexpireDate.text.toString()
                if(!isButtonClicked){
                    val PreviousStatus=Payment.status
                    updatepaymentStatus=PreviousStatus
                }
                else if (isPaid){ updatepaymentStatus="Paid" }
                else{ updatepaymentStatus="Pending" }
                val payment= VendorPaymentEntity(Payment.id,name,amount,date,updatepaymentStatus,Payment.VendorId)
                userupdatelistener?.onuserupdate(payment)
                Toast.makeText(context,"Data Added",Toast.LENGTH_SHORT).show()
                dismiss()
            }
            else{
            val name=editTextName.text.toString()
            val amount=editTextAmount.text.toString().toFloat()
            val date=vendorexpireDate.text.toString()
                if(isPaid){ paymentStatus="Paid" }
                else{ paymentStatus="Pending" }
            val payment= VendorPaymentEntity(0,name,amount,date,paymentStatus, VendorId!!)
            userDataListener?.onUserDataEntered(payment)
            Toast.makeText(context,"Data Added",Toast.LENGTH_SHORT).show()
            dismiss()
            } } }
    override fun onResume() {
        super.onResume()
        val Payment: VendorPaymentEntity?=arguments?.getParcelable("VendorPayment")
        if(Payment!=null){
            editTextName.setText(Payment.name.toString())
            editTextAmount.setText(Payment.amount.toString())
            vendorexpireDate.text = Payment.date

            if (Payment?.status == "Paid") { vendorButtonPaid.performClick() }
            else { vendorButtonPending.performClick() }
        }
    }
    private fun showDatePicker() {
        val constraint=CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraint.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDate
            val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
            val selectedMonth = selectedCalendar.get(Calendar.MONTH)
            val selectedYear = selectedCalendar.get(Calendar.YEAR)
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            vendorexpireDate.setText(formattedDate)
        }
        datePicker.show(fragmentManager, "datePicker")
    }

    fun setButtonBackground(button:Button,isSelected:Boolean){
        var backgroundcolor=if(isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList=ContextCompat.getColorStateList(requireContext(),backgroundcolor)
    }
}