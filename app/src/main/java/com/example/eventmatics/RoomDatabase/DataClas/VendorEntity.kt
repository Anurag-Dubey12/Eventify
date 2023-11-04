package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Vendor")
@Parcelize
data class VendorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "Vendor_Name")
    val name: String,
    @ColumnInfo(name = "Vendor_Category")
    val category: String,
    @ColumnInfo(name = "Vendor_Note")
    val note: String,
    @ColumnInfo(name = "Vendor_Estimated")
    val estimatedAmount: String,
    @ColumnInfo(name = "Vendor_Balance")
    val balance: String,
    @ColumnInfo(name = "Vendor_Pending")
    val remaining: String,
    @ColumnInfo(name = "Vendor_Paid")
    val paid: String,
    @ColumnInfo(name = "Vendor_PhoneNumber")
    val phoneNumber: String,
    @ColumnInfo(name = "Vendor_EmailId")
    val emailid: String,
    @ColumnInfo(name = "Vendor_Website")
    val website: String,
    @ColumnInfo(name = "Vendor_Address")
    val address: String
) : Parcelable

