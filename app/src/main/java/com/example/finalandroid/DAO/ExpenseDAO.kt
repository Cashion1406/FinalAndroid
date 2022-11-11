package com.example.finalandroid.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDAO {


    @Insert
    fun insert(expense: Expense)


    @Update(entity = Expense::class)
    fun realUpdate(expenseUpdate: ExpenseUpdate)

    @Entity
    class ExpenseUpdate(
        val expense_id: Int,
        val expense_name: String,
        val expense_price: Double,
        val expense_time: String,
        val expense_desc: String,
        val trip_id: Int

    )

    @Delete
    fun delete(expense: Expense)

    @Query("DELETE FROM EXPENSE_TABLE")
    fun deleteALL()


    @Query("SELECT * FROM expense_table")
    fun getAllExpense(): LiveData<List<Expense>>

    @Query("SELECT * FROM expense_table WHERE trip_id =:tripID ORDER BY expense_id DESC")
    fun getExpense(tripID: Int): Flow<List<Expense>>
}