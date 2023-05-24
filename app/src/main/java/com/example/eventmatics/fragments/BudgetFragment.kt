package com.example.eventmatics.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
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
    interface UserDataListener {
        fun onUserDataEntered(userData: Paymentinfo)
    }

    private var userDataListener: UserDataListener? = null

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
        etName = view.findViewById(R.id.editTextName)
        etAmount = view.findViewById(R.id.editTextAmount)
        buttonPending = view.findViewById(R.id.buttonPending)
        buttonPaid = view.findViewById(R.id.buttonPaid)
        etDate = view.findViewById(R.id.editTextDate)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        buttonPending.setOnClickListener {
            buttonPending.isSelected = true
            buttonPaid.isSelected = false
        }

        buttonPaid.setOnClickListener {
            buttonPending.isSelected = false
            buttonPaid.isSelected = true
        }
        buttonSubmit.setOnClickListener {
            val name = etName.text.toString()
            val amount = etAmount.text.toString().toFloat()
            val paymentStatus = if (buttonPending.isSelected) "Pending" else "Paid"
            val date = etDate.text.toString()

            val userData = Paymentinfo(name, amount, paymentStatus, date)
            userDataListener?.onUserDataEntered(userData)

            dismiss()
        }

    }


}

