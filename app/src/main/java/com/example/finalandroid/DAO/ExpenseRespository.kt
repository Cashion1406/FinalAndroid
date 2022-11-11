package com.example.finalandroid.DAO


import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ExpenseRespository(val expenseDAO: ExpenseDAO) {


    fun addExpense(expense: Expense) {
        expenseDAO.insert(expense)

    }


    fun realExpenseUpdate(expenseUpdate: ExpenseDAO.ExpenseUpdate) {

        expenseDAO.realUpdate(expenseUpdate)
    }


    fun deleteExpense(expense: Expense) {
        expenseDAO.delete(expense)

    }

    fun getAllExpense(trip_id: Int): Flow<List<Expense>> {
        return expenseDAO.getExpense(trip_id)
    }

    fun getexpensev2(): LiveData<List<Expense>> {
        return expenseDAO.getAllExpense()

    }

}