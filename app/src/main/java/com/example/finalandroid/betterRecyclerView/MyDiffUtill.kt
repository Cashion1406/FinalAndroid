package com.example.finalandroid.betterRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.finalandroid.DAO.TripModel

class MyDiffUtill(val oldList: List<TripModel>, val newList: List<TripModel>): DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
      return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}