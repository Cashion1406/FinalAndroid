package com.example.finalandroid.DAO

import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalandroid.DAO.TripModel


class TripRespository(val tripDAO: TripDAO) {

    //fetch all trip data
    val fetchAllTrip: LiveData<List<TripModel>> = tripDAO.getAlltrip()

    fun addtrip(tripModel: TripModel) {
        tripDAO.insert(tripModel)

    }

    fun updatetrip(tripUpdate: TripDAO.TripUpdate) {
        return tripDAO.update(tripUpdate)

    }

    fun gettrip(trip_id: Int): LiveData<TripModel> {
        return tripDAO.gettrip(trip_id)
    }

    fun delete(tripModel: TripModel) {
        tripDAO.delete(tripModel)

    }


    fun deleteAll() {
        tripDAO.deleteALL()

    }

    fun search(search: String): Flow<List<TripModel>> {

        return tripDAO.getv2(search)
    }

}