package com.example.eventmatics.fragments


import android.content.Context
import android.os.Bundle
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
import com.example.eventmatics.SQLiteDatabase.Dataclass.Paymentinfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class BudgetFragment(private val context: Context, private val fragmentManager: FragmentManager,
                     private val budgetId: Long?) : BottomSheetDialogFragment() {
    private lateinit var etName: EditText
    private lateinit var etAmount: EditText
    private lateinit var buttonPending: MaterialButton
    private lateinit var buttonPaid: MaterialButton
    private lateinit var etDate: TextView
    private lateinit var buttonSubmit: MaterialButton
    var status:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        return view
    }
    interface OnDataEnter{
        fun ondataenter(userdata: Paymentinfo)
    }
    private var userdataenter:OnDataEnter?=null

    fun setUserDataListner(listener:OnDataEnter){
        userdataenter=listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
              // Initialize views
        etName = view.findViewById(R.id.editTextName)
        etAmount = view.findViewById(R.id.editTextAmount)
        buttonPending = view.findViewById(R.id.buttonPending)
        buttonPaid = view.findViewById(R.id.buttonPaid)
        etDate = view.findViewById(R.id.editTextDate)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        clearFieldsAndButtons()
        etDate.setOnClickListener {
            showDatePicker()
        }
        buttonPending.setOnClickListener {
            setButtonBackground(buttonPending,true)
            setButtonBackground(buttonPaid,false)
        }
        buttonPaid.setOnClickListener {
            setButtonBackground(buttonPaid,true)
            setButtonBackground(buttonPending,false)

        }
        // Set click listener for the "Submit" button
        buttonSubmit.setOnClickListener {

            val name = etName.text.toString()
            val amount = etAmount.text.toString().toFloat()
            val date = etDate.text.toString()

            if(buttonPaid.isClickable){status=buttonPaid.text.toString()}
            if(buttonPending.isClickable){status=buttonPending.text.toString()}
//            val payment=Paymentinfo(0,name,amount,date,status, budgetId.toInt())
            val payment= Paymentinfo(0,name,amount,date,status, budgetId!!)
            userdataenter?.ondataenter(payment)
            Toast.makeText(context,"Data Added",Toast.LENGTH_SHORT).show()
//            amountpassing()

            dismiss()
        }


    }
    private fun clearFieldsAndButtons() {
        etName.text.clear()
        etAmount.text.clear()
        etDate.text = null
        setButtonBackground(buttonPending, false)
        setButtonBackground(buttonPaid, false)
    }
    override fun onResume() {
        super.onResume()

        // Clear EditText fields and reset button states when fragment is shown
        clearFieldsAndButtons()
    }

    fun getSharedPreference(context: Context, key: String): String?{
        val sharedvalue=context.getSharedPreferences("Database", Context.MODE_PRIVATE)
        return sharedvalue.getString(key,null)
    }
    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
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

    fun setButtonBackground(button: Button, isSelected: Boolean) {
        val backgroundColor = if (isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
    }
}

