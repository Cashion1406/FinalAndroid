package com.example.finalandroid.DAO

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


class BackUpModel(



    val triplist: List<TripModel> = ArrayList(),
    val expenseList: List<Expense> = ArrayList()

)
