package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Vendor_Payment")
@Parcelize
data class VendorPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    val id: Int = 0,
    @ColumnInfo(name = "Vendor_payment_name")
    val name: String,
    @ColumnInfo(name = "Vendor_payment_amount")
    val amount: Float,
    @ColumnInfo(name = "Vendor_payment_date")
    val date: String,
    @ColumnInfo(name = "Vendor_payment_status")
    val status: String,
    @ColumnInfo(name = "Vendor_payment_id")
    val VendorId: Long
) : Parcelable
