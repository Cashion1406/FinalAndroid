package com.example.finalandroid.fragments

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationProvider
import android.os.Bundle
import android.provider.CallLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.finalandroid.DAO.Expense
import com.example.finalandroid.R
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import kotlinx.android.synthetic.main.fragment_expense_dialog.*
import java.text.SimpleDateFormat
import java.util.*


class ExpenseDialog : DialogFragment() {


    private val args by navArgs<ExpenseDialogArgs>()

    private lateinit var expenseViewModel: ExpenseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_expense_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseViewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]

        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window!!.setBackgroundDrawable(
            getDrawable(
                requireContext(),
                R.drawable.round_trip_detail
            )
        )
        getCurrentDate()

        btn_add_expense.setOnClickListener {

            addExpense()
        }
        ed_add_expense_date.setOnClickListener {

            getDate()
        }


    }


    private fun getCurrentDate() {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.US)
        val getdate = Calendar.getInstance()
        val currentdate = simpleDateFormat.format(getdate.time)
        ed_add_expense_date.setText(currentdate)
    }


    private fun addExpense() {

        val name = ed_add_expense_name.text.toString().trim { it <= ' ' }
        val price = ed_add_expense_price.text.toString().trim { it <= ' ' }
        val date = ed_add_expense_date.text.toString().trim { it <= ' ' }
        val desc = ed_add_expense_desc.text.toString().trim { it <= ' ' }
        val trip_id = args.expenseInfo.id


        if (name.isNotEmpty() && price.isNotEmpty() && date.isNotEmpty() && desc.isNotEmpty()) {

            val expense =
                Expense(0, name, price.toDouble(), date, desc, trip_id)

            expenseViewModel.addExpense(expense)
            ed_add_expense_name.text?.clear()
            ed_add_expense_price.text?.clear()
            ed_add_expense_desc.text?.clear()
            ed_add_expense_date.text?.clear()
            dialog!!.dismiss()
            Toast.makeText(requireContext(), "ADDED EXPENSE", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(requireContext(), "SHITY INPUT", Toast.LENGTH_SHORT).show()
        }


    }


    fun getDate() {

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val getdate = Calendar.getInstance()


        val datepicker = DatePickerDialog(
            requireContext(),
            { datepicker, year, month, dayOfMonth ->
                val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR, year)
                selectDate.set(Calendar.MONTH, month)
                selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val date = simpleDateFormat.format(selectDate.time)

                ed_add_expense_date.setText(date.toString())

            },
            getdate.get(Calendar.YEAR),
            getdate.get(Calendar.MONTH),
            getdate.get(Calendar.DAY_OF_MONTH)
        )

        datepicker.show()

    }


}