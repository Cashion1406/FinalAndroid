package com.example.finalandroid.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.example.finalandroid.fragments.dashboard_fragmentDirections
import kotlinx.android.synthetic.main.trip_row.view.*


class tripAdapter :
    RecyclerView.Adapter<tripAdapter.ViewHolder>() {

    private var trips: List<TripModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.trip_row,
                parent,
                false
            )

        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = trips[position]

        holder.tvName.text = item.name

        holder.tvDate.text = item.date
        holder.cbRisk.setOnCheckedChangeListener(null)
        holder.cbRisk.isChecked = item.riskmanagement == "true"
        holder.itemView.setOnClickListener {
            val action = dashboard_fragmentDirections.actionDashboardFragmentToEdittrip(item)
            holder.itemView.findNavController().navigate(action)

        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return trips.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to

        val tvName = view.trip_name

        val tvDate = view.trip_date


        val cbRisk = view.cb_trip_risk
        val main = view.llMain


    }

    fun setTrip(trip: List<TripModel>) {
        this.trips = trip
        notifyDataSetChanged()

    }

    fun gettrip(position: Int): TripModel {
        return trips[position]
    }

}
