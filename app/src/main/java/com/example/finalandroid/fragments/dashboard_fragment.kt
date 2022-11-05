package com.example.finalandroid.fragments

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalandroid.DAO.TripModel
import com.example.finalandroid.MainActivity
import com.example.finalandroid.R
import com.example.finalandroid.WelcomeActivity
import com.example.finalandroid.adapter.tripAdapter
import com.example.finalandroid.viewmodel.TripViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.FieldIndex
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard_fragment.*
import java.util.*
import kotlin.collections.ArrayList


class dashboard_fragment : Fragment() {
    private lateinit var tripAdapter: tripAdapter

    private lateinit var tripviewmode: TripViewModel

    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripviewmode = ViewModelProvider(this)[TripViewModel::class.java]
/*

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
             Toast.makeText(
                 requireContext(),
                 FirebaseAuth.getInstance().currentUser!!.email.toString(),
                 Toast.LENGTH_SHORT
             ).show()
           return

        } else {
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()
        }
*/

        //loadUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).supportActionBar?.title = ""
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_dashboard_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        setUser()
        viewTrip()

        img_search.setOnClickListener {


            user_reveal.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            val reveal =
                if (reveal_search_bar.visibility == View.GONE) View.VISIBLE else View.GONE
            reveal_search_bar.visibility = reveal
            sv_trip.isIconified = false

            //reveal_search_bar.layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING)
        }
        sv_trip.setOnCloseListener {

            reveal_search_bar.visibility = View.GONE


            true
        }
/*

        btn_float.setOnClickListener {
            findNavController().navigate(R.id.addtrip_fragment)

        }*/
        tv_user_name.setOnLongClickListener {

            click()
            return@setOnLongClickListener true

        }
        tv_user_name.setOnClickListener {

            /*  val action =
                  dashboard_fragmentDirections.actionDashboardFragmentToUserNameDialog(tv_user_name.text.toString())

              findNavController().navigate(action)*/
            val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
            Toast.makeText(
                requireContext(),
                FirebaseAuth.getInstance().currentUser!!.uid,
                Toast.LENGTH_SHORT
            ).show()


            FirebaseAuth.getInstance().signOut()
            sharedPreferences.edit().remove("new_UID").apply()

            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()

        }

        sv_trip.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                filterlist(newText)

                return true
            }
        })
    }

    /*  private fun loadUser() {

          val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

          val islogin = sharedPreferences.getString("new_UID", "1")

          if (islogin == "1") {

              val getUID = requireActivity().intent.getStringExtra("UID")

              Toast.makeText(requireContext(), getUID.toString(), Toast.LENGTH_SHORT).show()
              if (getUID != null) {

                  setUser(getUID)

                  sharedPreferences.edit().putString("new_UID", getUID).apply()

              } else {
                  startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                  requireActivity().finish()

              }

          } else {

              setUser(islogin)


          }


      }*/

   /* fun setUser() {
        db = FirebaseFirestore.getInstance()
        if (context != null) {

            db.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).get()
                .addOnSuccessListener {

                        task ->

                    tv_user_name.text = task.get("Name").toString()
                }


        }

    }*/


    fun viewTrip() {
        tripviewmode.getalltrip().observe(viewLifecycleOwner) { trip ->

            if (trip.isNotEmpty()) {

                rv_trip.visibility = View.VISIBLE
                tv_no_trip_text.visibility = View.GONE


                tripAdapter = tripAdapter()
                rv_trip.adapter = tripAdapter
                rv_trip.layoutManager = LinearLayoutManager(requireContext())
                val divider = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
                divider.setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        androidx.appcompat.R.drawable.abc_list_divider_material
                    )!!
                )
                rv_trip.addItemDecoration(divider)//*

                //init trip adapter with passed param


                tripviewmode.getalltrip().observe(viewLifecycleOwner) { trip ->
                    tripAdapter.setTrip(trip)
                }


                //swipe to delete
                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val tripos: TripModel = tripAdapter.gettrip(viewHolder.adapterPosition)



                        tripviewmode.deletetrip(tripos)
                        Toast.makeText(
                            requireContext(),
                            viewHolder.adapterPosition.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        Snackbar.make(rv_trip, "Delete " + tripos.name, Snackbar.LENGTH_LONG)
                            .setAction(
                                "Undo", View.OnClickListener {
                                    tripviewmode.addtrip(tripos)

                                }
                            ).show()
                    }

                }).attachToRecyclerView(rv_trip)
            } else {
                rv_trip.visibility = View.GONE
                tv_no_trip_text.visibility = View.VISIBLE

            }

        }


    }


    fun filterlist(text: String) {
        tripAdapter = tripAdapter()

        val fillist: ArrayList<TripModel> = ArrayList()

        tripviewmode.getalltrip().observe(viewLifecycleOwner) {

                trip ->

            for (t in trip) {

                if (t.name.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))

                ) {
                    fillist.add(t)

                }


            }
            if (fillist.isEmpty()) {
                rv_trip.visibility = View.GONE
                tv_no_trip_text.visibility = View.VISIBLE
            } else {

                rv_trip.visibility = View.VISIBLE
                tv_no_trip_text.visibility = View.GONE
            }

        }

        tripAdapter.setTrip(fillist)

    }


    fun click() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("U SURE MATE ?")
        builder.setTitle("RESET ALL")
        builder.setIcon(R.drawable.ic_launcher_foreground)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            tripviewmode.deleteAll()
            dialogInterface.dismiss()

        }

        builder.setNegativeButton("No") { lmao, which ->

            lmao.dismiss()

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

}