package com.example.finalandroid.adapter


import android.animation.LayoutTransition
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.finalandroid.DAO.Expense
import com.example.finalandroid.R
import com.example.finalandroid.fragments.edittripDirections
import kotlinx.android.synthetic.main.expense_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class ExpenseAdapter :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    private var expenses: List<Expense> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.expense_row,
                parent,
                false
            )

        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = expenses[position]
        holder.tvName.text = item.expense_name
        holder.tvDescription.text = item.expense_desc
        holder.tvPrice.text = item.expense_price.toString() + " $"
        holder.tvDate.text = item.expense_time
        holder.itemView.tv_top.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)


        holder.itemView.setOnClickListener {

            val v =
                if (holder.itemView.tv_bottom.visibility == View.GONE) View.VISIBLE else View.GONE
            TransitionManager.beginDelayedTransition(holder.itemView.tv_top, AutoTransition())

            holder.itemView.tv_bottom.visibility = v


        }

        holder.itemView.setOnLongClickListener() {

            val action = edittripDirections.actionEdittripToEditExpenseDialog(item)

            holder.itemView.findNavController().navigate(action)
            true
        }


    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return expenses.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to

        val tvName = view.expense_name
        val tvPrice = view.expense_price
        val tvDate = view.expense_date
        val tvDescription = view.expense_desc


    }

    fun setExpense(expense: List<Expense>) {
        this.expenses = expense
        notifyDataSetChanged()

    }

    fun getExpense(position: Int): Expense {
        return expenses[position]
    }

}
