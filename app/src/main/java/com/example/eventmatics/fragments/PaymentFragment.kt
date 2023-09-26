package com.example.eventmatics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.razorpay.Checkout
import org.json.JSONException
import org.json.JSONObject


class PaymentFragment : Fragment() {
    lateinit var  amountEdt: EditText
    lateinit var payBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(com.example.eventmatics.R.layout.fragment_payment, container, false)
        amountEdt = view.findViewById(com.example.eventmatics.R.id.idEdtAmount);
        payBtn = view.findViewById(com.example.eventmatics.R.id.idBtnPay);
        payBtn.setOnClickListener {
            val samount = amountEdt.text.toString()
            val amount = Math.round(samount.toFloat() * 100)
            val checkout = Checkout()


            checkout.setKeyID("Enter your key id here")

            // set image
            checkout.setImage(com.example.eventmatics.R.drawable.event_management)

            // initialize json object
            val `object` = JSONObject()
            try {
                // to put name
                `object`.put("name", "Geeks for Geeks")

                // put description
                `object`.put("description", "Test payment")

                // to set theme color
                `object`.put("theme.color", "")

                // put the currency
                `object`.put("currency", "INR")

                // put amount
                `object`.put("amount", amount)

                // put mobile number
                `object`.put("prefill.contact", "9284064503")

                // put email
                `object`.put("prefill.email", "chaitanyamunje@gmail.com")

                // open razorpay to checkout activity
//                checkout.open(this@requireContext(), `object`)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return view
    }
    fun onPaymentSuccess(s: String) {
        // this method is called on payment success.
        Toast.makeText(context, "Payment is successful : $s", Toast.LENGTH_SHORT).show()
    }

    fun onPaymentError(i: Int, s: String) {
        // on payment failed.
        Toast.makeText(context, "Payment Failed due to error : $s", Toast.LENGTH_SHORT).show()
    }

    }
