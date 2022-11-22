package com.example.finalandroid.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalandroid.DAO.Expense
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.example.finalandroid.adapter.ExpenseAdapter
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.example.finalandroid.viewmodel.TripViewModel
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edittrip.*
import java.text.NumberFormat
import java.util.*


class edittrip : Fragment() {
    private val args by navArgs<edittripArgs>()
    private lateinit var currentTrip: TripModel

    private lateinit var expenseList: List<Expense>

    private lateinit var expenseChartList: ArrayList<BarEntry>


    private lateinit var expenseViewModel: ExpenseViewModel

    private lateinit var tripViewModel: TripViewModel

    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_edittrip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        tripViewModel = ViewModelProvider(this)[TripViewModel::class.java]

        currentTrip = args.currentTrip

        fetchtripinfo()
        viewExpense()
        viewExpenseChart()
        btn_edit_trip.setOnClickListener {

            val action = edittripDirections.actionEdittripToTripDialog(currentTrip)

            findNavController().navigate(action)
        }

        open_expense.setOnClickListener {
            val action = edittripDirections.actionEdittripToExpenseDialog(args.currentTrip)
            findNavController().navigate(action)
        }

        expenseViewModel.getExpense(args.currentTrip.id)
            .observe(viewLifecycleOwner) { expenses ->
                expenseList = expenses
                if (expenseList.isEmpty()) {
                    tv_totalprice.text = "Total Expense "
                }
                totalprice()
            }
    }

    private fun viewExpenseChart() {

        expenseViewModel.getExpense(args.currentTrip.id).observe(viewLifecycleOwner) {

                it ->

            expenseChartList = ArrayList<BarEntry>()
            val arrayListX = ArrayList<String>()
            if (expenseChartList.isEmpty()) {

                expense_barchart.clear()
            }


            for (expense in it.indices) {

                expenseChartList.add(
                    BarEntry(
                        expense.toFloat(),
                        it[expense].expense_price.toFloat()
                    )
                )

                arrayListX.add(it[expense].expense_name)
                val barDataSet = BarDataSet(expenseChartList, "")

                val data = BarData(barDataSet)

                expense_barchart.data = data

                expense_barchart.legend.isEnabled = false
                expense_barchart.description.isEnabled = false

                expense_barchart.animateY(1700)

                expense_barchart.xAxis.textSize = 13f
                expense_barchart.extraBottomOffset = 10f

                val color: ArrayList<Int> = ArrayList()

                for (i in ColorTemplate.MATERIAL_COLORS) {

                    color.add(i)
                }
                for (j in ColorTemplate.VORDIPLOM_COLORS) {
                    color.add(j)

                }


                expense_barchart.axisLeft.textSize = 13f
                expense_barchart.axisLeft.setDrawGridLines(false)

                barDataSet.valueTextSize = 14f
                barDataSet.colors = color
                val xaxis: XAxis = expense_barchart.xAxis
                xaxis.setDrawGridLines(false)
                xaxis.position = XAxis.XAxisPosition.BOTTOM
                xaxis.valueFormatter = IndexAxisValueFormatter(arrayListX)
                xaxis.granularity = 1f;
                xaxis.isGranularityEnabled = true;
                expense_barchart.axisRight.isEnabled = false


            }

            expense_barchart.setNoDataText("No Expense Data Available");
            val paint: Paint = expense_barchart.getPaint(Chart.PAINT_INFO)
            paint.textSize = 70f
            paint.color = resources.getColor(R.color.very_purple)
            expense_barchart.notifyDataSetChanged()
            expense_barchart.invalidate()

        }


    }


    fun fetchtripinfo() {


        tripViewModel.gettrip(args.currentTrip.id).observe(viewLifecycleOwner) {

                trip ->

            currentTrip = trip

            ed_trip_name.text = trip.name
            trip_destination.text = trip.destination
            trip_date.text = trip.date
            trip_desc.text = trip.description
            cb_trip_info_risk.isChecked = trip.riskmanagement.toBoolean()
            when (trip.transpotation) {
                "Plane" -> {
                    trip_car.visibility = View.GONE
                    trip_sea.visibility = View.GONE

                }
                "Sea" -> {
                    trip_plane.visibility = View.GONE
                    trip_car.visibility = View.GONE
                }
                else -> {
                    trip_plane.visibility = View.GONE
                    trip_sea.visibility = View.GONE

                }
            }

        }

    }

    fun viewExpense() {

        expenseViewModel.getExpense(args.currentTrip.id).observe(viewLifecycleOwner) {

                expense ->
            if (expense.isNotEmpty()) {


                rv_expense.visibility = View.VISIBLE


                expenseAdapter = ExpenseAdapter()
                rv_expense.adapter = expenseAdapter


                rv_expense.layoutManager = LinearLayoutManager(requireContext())


                expenseViewModel.getExpense(args.currentTrip.id)
                    .observe(viewLifecycleOwner) { expense ->
                        expenseAdapter.setExpense(expense)
                    }

                //swipe to delete
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val expensepos: Expense =
                            expenseAdapter.getExpense(viewHolder.adapterPosition)

                        expenseViewModel.deleteExpense(expensepos)
                        Snackbar.make(
                            rv_expense,
                            "Delete " + expensepos.expense_name,
                            Snackbar.LENGTH_LONG
                        )
                            .setAction(
                                "Undo", View.OnClickListener {
                                    expenseViewModel.addExpense(expensepos)
                                }
                            ).show()
                    }

                }).attachToRecyclerView(rv_expense)

            } else {

                rv_expense.visibility = View.GONE

            }
        }


    }

    fun totalprice() {
        var total = 0.0


        val format = NumberFormat.getCurrencyInstance(Locale.US)


        for (expenese in expenseList) {
            total += expenese.expense_price
            tv_totalprice.text = format.format(total)
        }
    }


}