package com.example.finalandroid.fragments


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.example.finalandroid.adapter.tripAdapter
import com.example.finalandroid.viewmodel.TripViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_addtrip_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class addtrip_fragment : Fragment() {

    private lateinit var tripviewmode: TripViewModel
    private lateinit var tripAdapter: tripAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripviewmode = ViewModelProvider(this)[TripViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_addtrip_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tripNameFocus()
        tripDestionationFocus()

        getCurrentDate()

        btn_add_trip.setOnClickListener {
            addtrip()
        }
        ed_add_trip_date.setOnClickListener {
            getDateRange()
        }


    }

    private fun getDateRange() {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yy", Locale.US)
        val dataRange =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Trip Duration ")
                .build()
        dataRange.show(childFragmentManager, "data_range")

        dataRange.addOnPositiveButtonClickListener {

                date ->
            //ed_add_trip_date.setText(dataRange.headerText)
            val begin = date.first
            val end = date.second
            ed_add_trip_date.setText(
                simpleDateFormat.format(begin) + " - " + simpleDateFormat.format(
                    end
                )
            )


        }
    }

    fun getDate() {

        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val getdate = Calendar.getInstance()
        val datepicker = DatePickerDialog(
            requireContext(),
            { datepicker, year, month, dayOfMonth ->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR, year)
                selectDate.set(Calendar.MONTH, month)
                selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val date = simpleDateFormat.format(selectDate.time)

                ed_add_trip_date.setText(date.toString())

            },
            getdate.get(Calendar.YEAR),
            getdate.get(Calendar.MONTH),
            getdate.get(Calendar.DAY_OF_MONTH)
        )

        datepicker.show()
    }


    private fun addtrip() {


        val name = ed_add_trip_name.text.toString().trim { it <= ' ' }
        val location = ed_add_trip_destination.text.toString().trim { it <= ' ' }
        val date = ed_add_trip_date.text.toString().trim { it <= ' ' }
        val desc = ed_add_trip_desc.text.toString().trim { it <= ' ' }

        val radioButtonID = rg_select.checkedRadioButtonId
        val radioButton = rg_select.findViewById<View>(radioButtonID) as? RadioButton
        val selectedtext = radioButton?.text as? String




        if (name.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty()) {

            val trip =
                TripModel(
                    0,
                    name,
                    location,
                    date,
                    desc,
                    ed_add_trip_risk.isChecked.toString(),
                    selectedtext


                )

            tripviewmode.addtrip(trip)
            ed_add_trip_name.text?.clear()
            ed_add_trip_destination.text?.clear()

            ed_add_trip_desc.text?.clear()

            Toast.makeText(requireContext(), trip.id.toString(), Toast.LENGTH_SHORT).show()

            val action = addtrip_fragmentDirections.actionAddtripFragmentToDashboardFragment()
            findNavController().navigate(action)
            view?.hideKeyboard()
        } else {

            Toast.makeText(requireContext(), "SHITY INPUT", Toast.LENGTH_SHORT).show()
        }


    }


    private fun getCurrentDate() {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val getdate = Calendar.getInstance()
        val currentdate = simpleDateFormat.format(getdate.time)
        ed_add_trip_date.setText(currentdate)
    }


    fun tripNameFocus() {

        ed_add_trip_name.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                register_email.helperText = validTripName()
            }

        }
    }

    fun validTripName(): String? {

        if (ed_add_trip_name.text!!.isEmpty()) {

            return "Empty Name"
        }

        return null
    }


    fun tripDestionationFocus() {

        ed_add_trip_destination.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                register_password.helperText = validTripDest()
            }

        }
    }

    fun validTripDest(): String? {

        if (ed_add_trip_name.text!!.isEmpty()) {

            return "Empty Destination"
        }
        return null
    }


    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}