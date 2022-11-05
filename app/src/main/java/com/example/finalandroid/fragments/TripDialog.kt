package com.example.finalandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.navArgs
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.example.finalandroid.adapter.tripAdapter

import com.example.finalandroid.viewmodel.TripViewModel

import kotlinx.android.synthetic.main.fragment_edittrip.*
import kotlinx.android.synthetic.main.fragment_trip_dialog.*
import java.util.*


class TripDialog : DialogFragment() {

    private val args by navArgs<TripDialogArgs>()

    private lateinit var tripviewmode: TripViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tripviewmode = ViewModelProvider(this)[TripViewModel::class.java]
        return inflater.inflate(R.layout.fragment_trip_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT + 900,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setBackgroundDrawable(
            getDrawable(
                requireContext(),
                R.drawable.round_trip_detail
            )
        )

        fetchtripinfo()

        btn_update_trip.setOnClickListener {
            updatetrip()
        }
        btn_cancle_add_trip.setOnClickListener {

            dialog!!.dismiss()
        }


    }


    fun fetchtripinfo() {


        ed_edit_trip_name.setText(args.tripinfo.name)
        ed_edit_trip_destination.setText(args.tripinfo.destination)
        ed_edit_trip_date.setText(args.tripinfo.date)
        ed_edit_trip_desc.setText(args.tripinfo.description)
        cb_edit_trip_risk.isChecked = args.tripinfo.riskmanagement.toBoolean()


    }


    fun updatetrip() {
        val name = ed_edit_trip_name.text.toString().trim { it <= ' ' }
        val location = ed_edit_trip_destination.text.toString().trim { it <= ' ' }
        val date = ed_edit_trip_date.text.toString().trim { it <= ' ' }
        val descripp = ed_edit_trip_desc.text.toString().trim { it <= ' ' }
        val risk = cb_edit_trip_risk.isChecked.toString()


        if (name.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty()) {

            val updatetrip = TripModel(
                args.tripinfo.id,
                name,
                location,
                date,
                descripp,
                risk,
                null
            )

            tripviewmode.updatetrip(updatetrip)



            Toast.makeText(requireContext(), "Updated successfully.", Toast.LENGTH_SHORT).show()

            dialog!!.dismiss()

        } else {
            Toast.makeText(requireContext(), "SHITY UPDATE,", Toast.LENGTH_SHORT).show()


        }
    }

    companion object {
        const val TAG = "EditTripDialog"
    }

}