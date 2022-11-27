package com.example.finalandroid.fragments


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_addtrip_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class addtrip_fragment : Fragment() {


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
            confirmation()
        }
        ic_calendar.setOnClickListener {
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

    private fun confirmation() {
        val name = ed_add_trip_name.text.toString().trim { it <= ' ' }
        val location = ed_add_trip_destination.text.toString().trim { it <= ' ' }
        val date = ed_add_trip_date.text.toString().trim { it <= ' ' }
        val desc = ed_add_trip_desc.text.toString().trim { it <= ' ' }
        val radioButtonID = rg_select.checkedRadioButtonId
        val radioButton = rg_select.findViewById<View>(radioButtonID) as? RadioButton
        val transportation = radioButton?.text as? String
        if (name.isNotEmpty() && location.isNotEmpty() && date.isNotEmpty()) {

            val trip =
                TripModel(
                    0,
                    name,
                    location,
                    date,
                    desc,
                    ed_add_trip_risk.isChecked.toString(),
                    transportation
                )
            val action = addtrip_fragmentDirections.actionAddtripFragmentToConfirmationTrip2(trip)
            findNavController().navigate(action)
            view?.hideKeyboard()
        } else {
            ed_add_trip_name.error = "Trip name is empty"

            FancyToast.makeText(
                requireContext(),
                "Please enter essential information",
                FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,
                false
            ).show()
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


    override fun onPause() {
        super.onPause()
        if (rg_select != null) {
            rg_select.clearCheck()
        }
    }
}