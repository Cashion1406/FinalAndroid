package com.example.finalandroid.DAO

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TripDAO {

    @Insert
    fun insert(trip: TripModel)

    @Update(entity = TripModel::class)
    fun update(trip: TripUpdate)

    @Entity
    class TripUpdate(
        val id: Int,
        val name: String,
        val destination: String,
        val date: String,
        val description: String,
        val riskmanagement: String

    )

    @Delete
    fun delete(trip: TripModel)

    @Query("DELETE FROM TRIP_TABLE")
    fun deleteALL()

    @Query("SELECT * FROM TRIP_TABLE ORDER BY ID DESC ")
    fun getAlltrip(): LiveData<List<TripModel>>

    @Query("SELECT * FROM TRIP_TABLE WHERE id =:tripId")
    fun gettrip(tripId: Int): LiveData<TripModel>

    @Query("SELECT * FROM trip_table WHERE name LIKE :quer")
    fun getv2(quer: String): Flow<List<TripModel>>

}