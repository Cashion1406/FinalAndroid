package com.example.finalandroid.fragments


import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.finalandroid.BuildConfig
import com.example.finalandroid.DAO.Expense
import com.example.finalandroid.R
import com.example.finalandroid.adapter.NetworkManagement.NetworkConnection
import com.example.finalandroid.viewmodel.ExpenseViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_expense_dialog.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ExpenseDialog : DialogFragment() {


    private val args by navArgs<ExpenseDialogArgs>()

    private lateinit var expenseViewModel: ExpenseViewModel


    private val REQUEST_CHECK_SETTING = 100
    private val UPDATE_INTERVAL: Long = 5000
    private val UPDATE_FASTEST: Long = 1000

    private lateinit var latestExpense: List<Expense>

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var mSettingClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private var geocoder: Geocoder? = null

    private var mRequestionLocationUPdate = false
    private var address: String? = null
    private lateinit var networkConnection: NetworkConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkConnection = NetworkConnection(requireContext())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mSettingClient = LocationServices.getSettingsClient(requireContext())


        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                mCurrentLocation = p0.lastLocation


                try {

                    networkConnection.observe(viewLifecycleOwner) { connection ->
                        if (connection) {
                            geocoder = Geocoder(requireContext(), Locale.getDefault())
                            val addresses: List<Address> = geocoder!!.getFromLocation(
                                mCurrentLocation!!.latitude,
                                mCurrentLocation!!.longitude, 1
                            )

                            val location =
                                addresses[0].adminArea.toString() + "\n" + addresses[0].getAddressLine(
                                    0
                                )

                            address = location

                        } else {
                            expenseViewModel.getLastedExepnse(args.expenseInfo.id)
                                .observe(viewLifecycleOwner) { expense ->
                                    latestExpense = expense
                                    
                                    for (e in latestExpense) {

                                        address = if (e.location!!.contains("Last known at")) {

                                            e.location.toString()
                                        } else {
                                            "Last known at" + "\n" + e.location.toString()

                                        }

                                    }
                                }
                        }

                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.i("CATN FETCH LOCATION", e.toString())
                }
            }
        }
        mLocationRequest =
            LocationRequest.create().setInterval(UPDATE_INTERVAL).setFastestInterval(UPDATE_FASTEST)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingRequest = builder.build()

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    mRequestionLocationUPdate = true
                    Toast.makeText(requireContext(), "LOcation Start", Toast.LENGTH_SHORT).show()
                    startlocate()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    if (p0 != null) {
                        if (p0.isPermanentlyDenied) {
                            openSetting()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()
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


    fun openSetting() {

        val intent = Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    fun startlocate() {
        mLocationSettingRequest?.let {
            mSettingClient?.checkLocationSettings(it)?.addOnSuccessListener {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return@addOnSuccessListener
                }
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest!!, mLocationCallback!!,
                    Looper.getMainLooper()
                )

            }
                ?.addOnFailureListener {

                    val status: Int = (it as ApiException).statusCode

                    when (status) {

                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                val rea = it as ResolvableApiException
                                rea.startResolutionForResult(
                                    requireActivity(),
                                    REQUEST_CHECK_SETTING
                                )
                            } catch (e: IntentSender.SendIntentException) {
                                Toast.makeText(
                                    requireContext(),
                                    "PENDING REQUEST CANT EXECUTE",
                                    Toast.LENGTH_SHORT
                                ).show()


                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                            Toast.makeText(
                                requireContext(),
                                "LOCATION SETTING BROKEN",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                }
        }
    }

    fun stoplocate() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback!!)
        Toast.makeText(requireContext(), "LOcation Stop", Toast.LENGTH_SHORT).show()
    }

    fun checkPER(): Boolean {

        val permissionState = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        if (mRequestionLocationUPdate && checkPER()) {
            startlocate()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mRequestionLocationUPdate) {

            stoplocate()
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
        var desc = ed_add_expense_desc.text.toString().trim { it <= ' ' }
        val trip_id = args.expenseInfo.id

        if (address != null && desc.isEmpty()) {

            desc = address.toString()
        } else if (address == null || address!!.isEmpty()) {
            address = "N/A"
            desc = address.toString()
        }


        if (name.isNotEmpty() && price.isNotEmpty() && date.isNotEmpty()) {

            val expense =
                Expense(0, name, price.toDouble(), date, desc, trip_id, address)

            expenseViewModel.addExpense(expense)
            ed_add_expense_name.text?.clear()
            ed_add_expense_price.text?.clear()
            ed_add_expense_desc.text?.clear()
            ed_add_expense_date.text?.clear()
            dialog!!.dismiss()
            Toast.makeText(requireContext(), "ADDED EXPENSE", Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(requireContext(), "Please fill essential detail", Toast.LENGTH_SHORT)
                .show()
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