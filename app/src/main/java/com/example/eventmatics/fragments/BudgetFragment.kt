package com.example.eventmatics.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.eventmatics.R
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Paymentinfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class BudgetFragment(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val budgetId: Long?,
    private val payment: Paymentinfo?
) : BottomSheetDialogFragment() {

    private lateinit var etName: EditText
    private lateinit var etAmount: EditText
    private lateinit var buttonPending: MaterialButton
    private lateinit var buttonPaid: MaterialButton
    private lateinit var etDate: TextView
    private lateinit var buttonSubmit: MaterialButton
    private var isPaid: Boolean = false
    private var isButtonClicked: Boolean = false
    private var status: String = ""
    private var updatedStatus: String = ""

    interface OnDataEnter {
        fun onDataEnter(userdata: Paymentinfo)
    }

    interface OnDataUpdated {
        fun onDataUpdate(userdata: Paymentinfo)
    }

    private var userDataEnter: OnDataEnter? = null
    private var userDataUpdate: OnDataUpdated? = null

    fun setUserDataListener(listener: OnDataEnter) {
        userDataEnter = listener
    }

    fun setUserDataUpdateListener(listener: OnDataUpdated) {
        userDataUpdate = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)
        etName = view.findViewById(R.id.editTextName)
        etAmount = view.findViewById(R.id.editTextAmount)
        buttonPending = view.findViewById(R.id.buttonPending)
        buttonPaid = view.findViewById(R.id.buttonPaid)
        etDate = view.findViewById(R.id.editTextDate)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        etDate.setOnClickListener { showDatePicker() }

        buttonPending.setOnClickListener {
            isPaid = false
            isButtonClicked = true
            setButtonBackground(buttonPending, true)
            setButtonBackground(buttonPaid, false)
        }

        buttonPaid.setOnClickListener {
            isPaid = true
            isButtonClicked = true
            setButtonBackground(buttonPaid, true)
            setButtonBackground(buttonPending, false)
        }

        buttonSubmit.setOnClickListener {
            val paymentData: Paymentinfo? = arguments?.getParcelable("Payment")
            if (paymentData != null) {
                val name = etName.text.toString()
                val amount = etAmount.text.toString().toFloat()
                val date = etDate.text.toString()
                updatedStatus = if (!isButtonClicked) {
                    paymentData.status
                } else if (isPaid) {
                    "Paid"
                } else {
                    "Pending"
                }
                val payment = Paymentinfo(
                    paymentData.id,
                    name,
                    amount,
                    date,
                    updatedStatus,
                    paymentData.budgetid
                )
                userDataUpdate?.onDataUpdate(payment)
                Toast.makeText(context, "Data Modified", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                val name = etName.text.toString()
                val amount = etAmount.text.toString().toFloat()
                val date = etDate.text.toString()
                status = if (isPaid) {
                    "Paid"
                } else {
                    "Pending"
                }
                val payment = Paymentinfo(0, name, amount, date, status, budgetId!!)
                userDataUpdate?.onDataUpdate(payment)
                Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val Payment_data: Paymentinfo? = arguments?.getParcelable("Payment")
        if (Payment_data != null) {
//        Log.d("Payment_Info","Details are:${Payment_data?.id},${Payment_data?.name}")
            etName.setText(Payment_data.name.toString())
            etAmount.setText(Payment_data.amount.toString())
            etDate.text = Payment_data.date
            Log.d("BudgetFragment", "onViewCreated: payment = ${Payment_data.name}")
            Log.d("BudgetFragment", "onViewCreated: etName = $etName")

            if (payment?.status == "Paid") {
                buttonPaid.performClick()
            } else {
                buttonPending.performClick()
            }
        }
//        clearFieldsAndButtons()
    }

    private fun showDatePicker() {
        val constraint = CalendarConstraints.Builder()
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
            etDate.setText(formattedDate)
        }

        datePicker.show(fragmentManager, "datePicker")
    }

    private fun setButtonBackground(button: Button, isSelected: Boolean) {
        val backgroundColor = if (isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
    }
}
