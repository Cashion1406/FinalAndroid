package com.example.finalandroid.DAO

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "trip_table")
data class TripModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val destination: String,
    val date: String,
    val description: String,
    val riskmanagement: String,
    val transpotation: String?=null



    ) : Parcelable
