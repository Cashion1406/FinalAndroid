package com.example.finalandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalandroid.DAO.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.exp

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {


    private val respository: ExpenseRespository

    init {
        val expenseDAO = TripDB.getDB(application).expenseDAO()
        respository = ExpenseRespository(expenseDAO)


    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            respository.addExpense(expense)

        }

    }

    fun deleteExpense(expense: Expense) {

        viewModelScope.launch(Dispatchers.IO) {

            respository.deleteExpense(expense)
        }

    }


    fun realUpdateExpense(expenseUpdate: ExpenseDAO.ExpenseUpdate) {

        viewModelScope.launch(Dispatchers.IO) {

            respository.realExpenseUpdate(expenseUpdate)
        }
    }

    fun getExpense(trip_id: Int): LiveData<List<Expense>> {

        return respository.getAllExpense(trip_id).asLiveData()
    }

    fun getLastedExepnse(trip_id: Int): LiveData<List<Expense>> {

        return respository.getlastedExpense(trip_id).asLiveData()
    }


    fun getallExpense(): LiveData<List<Expense>> {

        return respository.getexpensev2()
    }

}