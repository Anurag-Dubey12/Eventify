package com.example.eventmatics

import androidx.lifecycle.ViewModel
import com.example.eventmatics.SQLiteDatabase.Dataclass.Paymentinfo

class PaymentViewModel : ViewModel() {
    var paymentInfo: Paymentinfo? = null
}