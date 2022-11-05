package com.example.finalandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.finalandroid.DAO.Expense
import com.example.finalandroid.R
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.example.finalandroid.viewmodel.TripViewModel
import kotlinx.android.synthetic.main.fragment_edit_expense_diaglog.*

class EditExpenseDialog : DialogFragment() {

    private val args by navArgs<EditExpenseDialogArgs>()

    private lateinit var expenseViewModel: ExpenseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_edit_expense_diaglog, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        viewExpense(args.currentExpense)

        dialog!!.window!!.setBackgroundDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.round_trip_detail
            )
        )
        btn_edit_expense.setOnClickListener {

            updateExpense()
        }


    }

    private fun viewExpense(currentExpense: Expense) {

        ed_edit_expense_name.setText(currentExpense.expense_name)
        ed_edit_expense_price.setText(currentExpense.expense_price.toString())
        ed_edit_expense_date.setText(currentExpense.expense_time)
        ed_edit_expense_desc.setText(currentExpense.expense_desc)

    }


    private fun updateExpense() {
        val name = ed_edit_expense_name.text.toString().trim { it <= ' ' }
        val price = ed_edit_expense_price.text.toString().trim { it <= ' ' }
        val date = ed_edit_expense_date.text.toString().trim { it <= ' ' }
        val descrip = ed_edit_expense_desc.text.toString().trim { it <= ' ' }
        val tripid = args.currentExpense.trip_id


        if (name.isNotEmpty() && date.isNotEmpty() && date.isNotEmpty()) {

            val updateExpense = Expense(
                args.currentExpense.expense_id,
                name,
                price.toDouble(),
                date,
                descrip,
                tripid

            )

            expenseViewModel.updateExpense(updateExpense)
            Toast.makeText(requireContext(), "Updated successfully.", Toast.LENGTH_SHORT).show()
            dialog!!.dismiss()


        } else {
            Toast.makeText(requireContext(), "SHITY UPDATE,", Toast.LENGTH_SHORT).show()


        }
    }

}