package com.example.finalandroid.DAO

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "expense_table",
    foreignKeys = [ForeignKey(
        entity = TripModel::class,
        childColumns = ["trip_id"],
        parentColumns = ["id"],
        onDelete = CASCADE
    )]
)
class Expense(


    @PrimaryKey(autoGenerate = true)
    val expense_id: Int,
    val expense_name: String,
    val expense_price: Double,
    val expense_time: String,
    val expense_desc: String,
    val trip_id: Int,
    val location: String?


) : Parcelable
