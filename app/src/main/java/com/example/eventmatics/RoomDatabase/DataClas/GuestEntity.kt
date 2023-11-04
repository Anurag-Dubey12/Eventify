package com.example.eventmatics.RoomDatabase.DataClas

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity("Guest")
@Parcelize
data class GuestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "Guest_Name")
    val name: String,
    @ColumnInfo(name = "total_family_members")
    val totalFamilyMembers: String,
    @ColumnInfo(name = "note")
    val note: String,
    @ColumnInfo(name = "guest_status")
    val isInvitationSent: String,
    @ColumnInfo(name = "guest_contact")
    val phoneNumber: String,
    @ColumnInfo(name = "Guest_Acceptence_Status")
    val Acceptance: String,
    @ColumnInfo(name = "guest_address")
    val address: String
) : Parcelable

