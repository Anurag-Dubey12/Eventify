package com.example.eventmatics.SQLiteDatabase.Dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VendorPaymentinfo(
    val id: Int,
    val name: String,
    val amount: Float,
    val date: String,
    val status: String,
    val VendorId: Long
) : Parcelable
