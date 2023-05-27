package com.example.eventmatics.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BudgetFragment : BottomSheetDialogFragment() {
    private lateinit var etName: EditText
    private lateinit var etAmount: EditText
    private lateinit var buttonPending: Button
    private lateinit var buttonPaid: Button
    private lateinit var etDate: EditText
    private lateinit var buttonSubmit: Button

   interface PendingAmountListener{
       fun onPendingAmountSelected(amount:Float)
   }
    var pendingamountlistner:PendingAmountListener?=null
    interface PaidAmountListener{
        fun onPaidAmountSelected(amount:Float)
    }
    var paidamountlistner:PaidAmountListener?=null


    // Define an interface to communicate data to the parent activity or fragment
    interface UserDataListener {
        fun onUserDataEntered(userData: Paymentinfo)
    }

    private var userDataListener: UserDataListener? = null
    // Setter method for the UserDataListener
    fun setUserDataListener(listener: UserDataListener) {
        userDataListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        return view

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
        // Set click listener for the "Pending" button



        buttonPending.setOnClickListener {
            setButtonBackground(buttonPending,true)
            setButtonBackground(buttonPaid,false)

            val amount=etAmount.text.toString().toFloat()
            pendingamountlistner?.onPendingAmountSelected(amount)
        }
        buttonPaid.setOnClickListener {
            setButtonBackground(buttonPaid,true)
            setButtonBackground(buttonPending,false)

            val amount=etAmount.text.toString().toFloat()
            paidamountlistner?.onPaidAmountSelected(amount)
        }
        // Set click listener for the "Submit" button
        buttonSubmit.setOnClickListener {
            val name = etName.text.toString()
            val amount = etAmount.text.toString().toFloat()
            val date = etDate.text.toString()
            // Created a Paymentinfo object with the entered data
            val userData = Paymentinfo(name, amount, date)
            // Pass the userData object to the listener
            userDataListener?.onUserDataEntered(userData)

//            amountpassing()
            // Close the BottomSheetDialogFragment
            dismiss()
        }


    }


    fun setButtonBackground(button: Button, isSelected: Boolean) {
        val backgroundColor = if (isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList = ContextCompat.getColorStateList(requireContext(), backgroundColor)
    }


}

