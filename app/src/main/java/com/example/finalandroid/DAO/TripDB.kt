package com.example.finalandroid.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [TripModel::class, Expense::class],
    version = 1,
    exportSchema = false
)
abstract class TripDB : RoomDatabase() {

    abstract fun tripDAO(): TripDAO
    abstract fun expenseDAO(): ExpenseDAO


    companion object {
        @Volatile
        private var INSTANCE: TripDB? = null

        fun getDB(context: Context): TripDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance

            }
            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext, TripDB::class.java, "trip_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }

        }
    }
}