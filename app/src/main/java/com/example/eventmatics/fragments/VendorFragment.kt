package com.example.eventmatics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.eventmatics.R
import com.example.eventmatics.data_class.Paymentinfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VendorFragment : BottomSheetDialogFragment() {
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var vendorButtonPending: AppCompatButton
    private lateinit var vendorButtonPaid: AppCompatButton
    private lateinit var editTextDate: EditText
    private lateinit var buttonSubmit: AppCompatButton

    interface PendingAmountlistener{
        fun onPendingAmountSelected(amount:Float)
    }
    var pendingAmountlistener:PendingAmountlistener?=null

    interface PaidAmountListener{
        fun onPaidAmountSelected(amount:Float)
    }
    var paidAmountListener:PaidAmountListener?=null
    interface UserDataListener {
        fun onUserDataEntered(userData: Paymentinfo)
    }

    private var userDataListener: VendorFragment.UserDataListener? = null
    // Setter method for the UserDataListener
    fun setUserDataListener(listener: VendorFragment.UserDataListener) {
        userDataListener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_vendor, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName = view.findViewById(R.id.editTextName)
        editTextAmount = view.findViewById(R.id.editTextAmount)
        vendorButtonPending = view.findViewById(R.id.vendorbuttonPending)
        vendorButtonPaid = view.findViewById(R.id.vendorbuttonPaid)
        editTextDate = view.findViewById(R.id.editTextDate)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        //VendorPending amount listener to pass the amount value and changing button backgriund on click
        vendorButtonPending.setOnClickListener {
            setButtonBackground(vendorButtonPending,true)
            setButtonBackground(vendorButtonPaid,false)
            val amount=editTextAmount.text.toString().toFloat()
            pendingAmountlistener?.onPendingAmountSelected(amount)
        }
        //VendorPaid amount listener to pass the amount value and changing button backgriund on click
        vendorButtonPaid.setOnClickListener {
            setButtonBackground(vendorButtonPaid,true)
            setButtonBackground(vendorButtonPending,false)
            val amount=editTextAmount.text.toString().toFloat()
            paidAmountListener?.onPaidAmountSelected(amount)
        }
        buttonSubmit.setOnClickListener {
            val name=editTextName.text.toString()
            val amount=editTextAmount.text.toString().toFloat()
            val paymentStatus = if (vendorButtonPending.isSelected) "Pending" else "Paid"
            val date=editTextDate.text.toString()

            val userdata=Paymentinfo(name, amount, date)
//When the user clicks the buttonSubmit, the onClick listener is triggered.
//The listener retrieves the values entered in the EditText fields and creates a Paymentinfo object with the provided data.
//The userDataListener is then called with the userData as an argument to pass the entered payment information.
//Finally, the VendorFragment is dismissed, closing the bottom sheet dialog.
            userDataListener?.onUserDataEntered(userdata)
            dismiss()
        }
    }
    fun setButtonBackground(button:Button,isSelected:Boolean){
        var backgroundcolor=if(isSelected) R.color.light_blue else R.color.white
        button.backgroundTintList=ContextCompat.getColorStateList(requireContext(),backgroundcolor)
    }
}