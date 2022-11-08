package com.example.finalandroid.fragments


import android.Manifest
import android.app.DatePickerDialog
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
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
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_expense_dialog.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ExpenseDialog : DialogFragment() {


    private val args by navArgs<ExpenseDialogArgs>()

    private lateinit var expenseViewModel: ExpenseViewModel


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var mCurrentLocation: Location? = null
    private var mLocationRequest: LocationRequest? = null

    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        mLocationRequest = LocationRequest.create().apply {
            interval = 4000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }

        locationCallback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult) {


                for (location: Location in p0.locations) {


                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val address: List<Address> =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    if (address.isNotEmpty()) {
                        val country = address[0].adminArea
                        val city = address[0].getAddressLine(0)

                        ed_city_name.setText(city?.toString())
                        ed_country_name.setText(country?.toString())


                    } else {

                        Toast.makeText(requireContext(), "CANT FETCH LCOATION ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

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

        checkSettingLocation()
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

            //addExpense()


        }
        ed_add_expense_date.setOnClickListener {

            getDate()
        }


    }

    private fun getlocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            getlocationPermission()
            return
        }



        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->


            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            try {
                val address: List<Address> =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)

                if (address.isNotEmpty()) {
                    val country = address[0].adminArea
                    val city = address[0].getAddressLine(0)

                    ed_city_name.setText(city?.toString())
                    ed_country_name.setText(country?.toString())


                } else {

                    Toast.makeText(requireContext(), "CANT FETCH LCOATION ", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "Unable connect to Geocoder", Toast.LENGTH_LONG)
                    .show()

            }


        }
    }


    private fun getlocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 100) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //getlocation()
                checkSettingLocation()
            } else {

                Toast.makeText(requireContext(), "FUK NO LocATION FOR U ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getAddress(latLng: LatLng) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var fulladdress = ""
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if (addresses.isNotEmpty()) {
            address = addresses[0]
            fulladdress =
                address.getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex
            var city = address.getLocality();
            var state = address.getAdminArea();
            var country = address.getCountryName();
            var postalCode = address.getPostalCode();
            var knownName = address.getFeatureName(); // Only if available else return NULL
        } else {
            fulladdress = "Location not found"
        }
    }

    fun checkSettingLocation() {
        val request: LocationSettingsRequest? =
            mLocationRequest?.let {
                LocationSettingsRequest.Builder().addLocationRequest(it).build()
            }

        val client: SettingsClient = LocationServices.getSettingsClient(requireContext())

        val locationSettingRepsone: Task<LocationSettingsResponse> =
            request?.let { client.checkLocationSettings(it) } as Task<LocationSettingsResponse>

        locationSettingRepsone.addOnSuccessListener {

            startLocation()
        }
        locationSettingRepsone.addOnFailureListener {

                it ->
            if (it is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    it.startResolutionForResult(
                        requireActivity(),
                        100
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }


        }
    }


    fun startLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest!!, locationCallback,
            Looper.getMainLooper()
        )

    }

    fun stopLocation() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
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

    override fun onStop() {
        super.onStop()
        stopLocation()
    }

}