package com.example.finalandroid.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDAO {


    @Insert
    fun insert(expense: Expense)

    @Update
    fun update(expense: Expense)

    @Delete
    fun delete(expense: Expense)

    @Query("DELETE FROM EXPENSE_TABLE")
    fun deleteALL()


    @Query("SELECT * FROM expense_table")
    fun getAllExpense(): LiveData<List<Expense>>

    @Query("SELECT * FROM expense_table WHERE trip_id =:tripID ")
    fun getExpense(tripID: Int): Flow<List<Expense>>
}