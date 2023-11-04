package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Payment")
@Parcelize
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "payment_id")
    val id: Int = 0,
    @ColumnInfo(name = "payment_name")
    val name: String,
    @ColumnInfo(name = "payment_amount")
    val amount: Float,
    @ColumnInfo(name = "payment_date")
    val date: String,
    @ColumnInfo(name = "payment_status")
    val status: String,
    @ColumnInfo(name = "Budget_payment_id")
    val BudgetId: Long,
) : Parcelable
