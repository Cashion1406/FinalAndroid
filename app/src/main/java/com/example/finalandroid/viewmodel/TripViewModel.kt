package com.example.finalandroid.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.finalandroid.DAO.TripDAO
import com.example.finalandroid.DAO.TripDB
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.DAO.TripRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class TripViewModel(application: Application) : AndroidViewModel(application) {


    val tripList: LiveData<List<TripModel>>
    private val respository: TripRespository

    init {
        val tripDAO = TripDB.getDB(application).tripDAO()
        respository = TripRespository(tripDAO)
        tripList = respository.fetchAllTrip

    }

    fun addtrip(tripModel: TripModel) {
        viewModelScope.launch(Dispatchers.IO) {
            respository.addtrip(tripModel)
        }

    }


    fun deletetrip(tripModel: TripModel) {

        viewModelScope.launch(Dispatchers.IO) {

            respository.delete(tripModel)
        }

    }


    fun updatetrip(tripUpdate: TripDAO.TripUpdate) {

        viewModelScope.launch(Dispatchers.IO) {
            respository.updatetrip(tripUpdate)
        }

    }

    fun gettrip(tripId: Int): LiveData<TripModel> {


        return respository.gettrip(tripId)


    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {

            respository.deleteAll()
        }


    }


    fun getalltrip(): LiveData<List<TripModel>> {
        return tripList
    }

    fun search(search: String): LiveData<List<TripModel>> {

        return respository.search(search).asLiveData()
    }


}

