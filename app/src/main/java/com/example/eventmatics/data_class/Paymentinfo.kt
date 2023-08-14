package com.example.eventmatics.data_class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Paymentinfo(
    val id: Int,
    val name: String,
    val amount: Float,
    val date: String,
    val status: String,
    val budgetid: Long
) : Parcelable
