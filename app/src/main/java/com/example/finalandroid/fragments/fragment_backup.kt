package com.example.finalandroid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.finalandroid.DAO.BackUpModel
import com.example.finalandroid.DAO.Expense
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.R
import com.example.finalandroid.adapter.NetworkManagement.NetworkConnection
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.example.finalandroid.viewmodel.TripViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_backup.*


open class fragment_backup : Fragment() {
    private lateinit var tripList: List<TripModel>;
    private lateinit var expenselist: List<Expense>;

    private lateinit var tripViewModel: TripViewModel
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var networkConnection: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnection = NetworkConnection(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_backup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        networkConnection.observe(viewLifecycleOwner) { connection ->

            if (connection) {
                Toast.makeText(requireContext(), "OK WIFI", Toast.LENGTH_SHORT).show()
                iv_connection.visibility = View.GONE
                tv_network_title.visibility = View.GONE
                tv_network_desc.visibility = View.GONE
                btn_backup.isEnabled = true
                btn_backup.setOnClickListener {
                    uploadData()
                }

            } else {
                btn_backup.isEnabled = false
                Toast.makeText(requireContext(), "NOT OK WIFI", Toast.LENGTH_SHORT).show()
                iv_connection.visibility = View.VISIBLE
                tv_network_title.visibility = View.VISIBLE
                tv_network_desc.visibility = View.VISIBLE

            }
        }

        tripViewModel = ViewModelProvider(this)[TripViewModel::class.java]
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        tripViewModel.getalltrip().observe(viewLifecycleOwner) { trips -> tripList = trips }
        expenseViewModel.getallExpense()
            .observe(viewLifecycleOwner) { expense -> expenselist = expense }
    }

    private fun uploadData() {

        //Toast.makeText(requireContext(), FirebaseAuth.getInstance().currentUser!!.uid.toString(), Toast.LENGTH_SHORT).show()

        val db = Firebase.firestore


        val backUpModel = BackUpModel(tripList, expenselist)

        db.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update(FirebaseAuth.getInstance().currentUser!!.uid, backUpModel)
            .addOnSuccessListener { ok ->
                Toast.makeText(requireContext(), "Upload OK", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }


}


