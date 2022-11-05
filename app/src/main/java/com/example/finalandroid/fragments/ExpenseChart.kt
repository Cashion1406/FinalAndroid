package com.example.finalandroid.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.finalandroid.R
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.protobuf.Value
import kotlinx.android.synthetic.main.fragment_edittrip.*
import kotlinx.android.synthetic.main.fragment_expense_chart.*

class ExpenseChart : Fragment() {


    private lateinit var expenseViewModel: ExpenseViewModel

    private val args by navArgs<ExpenseChartArgs>()

    private lateinit var expenselist: ArrayList<BarEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]


        /*expenseViewModel.getExpense(args.currentChartExpense.id).observe(viewLifecycleOwner) {

                it ->

            expenselist = ArrayList<BarEntry>()
            val arrayListX = ArrayList<String>()

            if (expenselist.isEmpty()){

                expense_barchart.clear()
            }

            for (expense in it.indices) {

                expenselist.add(
                    BarEntry(
                        expense.toFloat(),
                        it[expense].expense_price.toFloat()
                    )
                )

                //Label For each Expense
                arrayListX.add(it[expense].expense_name)

                val barDataSet = BarDataSet(expenselist, "")

                val data = BarData(barDataSet)

                expense_barchart.data = data

                expense_barchart.legend.isEnabled = false
                expense_barchart.description.isEnabled = false

                expense_barchart.animateXY(2100, 2100)

                expense_barchart.xAxis.textSize = 11f


                expense_barchart.axisLeft.textSize = 11f
                expense_barchart.axisLeft.setDrawGridLines(false)

                barDataSet.valueTextSize = 13f

                val xaxis: XAxis = expense_barchart.xAxis
                xaxis.setDrawGridLines(false)
                xaxis.position = XAxis.XAxisPosition.BOTTOM
                xaxis.valueFormatter = IndexAxisValueFormatter(arrayListX)
                xaxis.granularity = 1f;
                xaxis.isGranularityEnabled = true;
                expense_barchart.axisRight.isEnabled = false


            }
            expense_barchart.notifyDataSetChanged()
            expense_barchart.invalidate()

        }*/
    }

}