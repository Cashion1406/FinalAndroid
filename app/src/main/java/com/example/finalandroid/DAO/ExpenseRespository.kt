package com.example.finalandroid.DAO


import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlin.math.exp

class ExpenseRespository(val expenseDAO: ExpenseDAO) {



    suspend fun addExpense(expense: Expense) {
        expenseDAO.insert(expense)

    }

    suspend fun updateExpense(expense: Expense) {
        expenseDAO.update(expense)

    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDAO.delete(expense)

    }
    fun getAllExpense(trip_id: Int): Flow<List<Expense>> {
        return expenseDAO.getExpense(trip_id)
    }

    fun getexpensev2(): LiveData<List<Expense>>{
        return  expenseDAO.getAllExpense()

    }

}