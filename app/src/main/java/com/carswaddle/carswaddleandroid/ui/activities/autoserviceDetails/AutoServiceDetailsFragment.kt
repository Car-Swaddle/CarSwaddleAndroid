package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.DateDisplayView
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.ImageLabel
import com.carswaddle.carswaddleandroid.ui.activities.schedule.LocationFragment
import com.carswaddle.carswaddleandroid.ui.activities.schedule.saltLakeAndProvo
import com.carswaddle.carswaddleandroid.util.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.math.RoundingMode


class AutoServiceDetailsFragment(val autoServiceId: String) : Fragment(), OnMapReadyCallback {

//    private lateinit var autoServicesListViewModel: AutoServicesListViewModel
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var viewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var dateDisplay: DateDisplayView
    private lateinit var mechanicNameTextView: TextView
    private lateinit var statusPillButton: Button
    private lateinit var vehicleImageLabel: ImageLabel
    private lateinit var oilTypeImageLabel: ImageLabel
    private lateinit var streetAddressImageLabel: ImageLabel
    private lateinit var locationMapView: MapView
    private lateinit var distanceBetweenTextView: TextView

    private var userLocation: Location? = null

    private var autoServiceLocation: Location? = null

    private val locationPermissionRequestCode = 1
    private val zoomLevel = 18.0f

    private lateinit var callback: LocationFragment.OnLocationSelectedListener
    private lateinit var map: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private lateinit var autoServiceDetailsViewModel: AutoServiceDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_autoservice_details, container, false)

        activity?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }

        dateDisplay = root.findViewById(R.id.date_display)
        mechanicNameTextView = root.findViewById(R.id.mechanic_name_text_view)
        statusPillButton = root.findViewById(R.id.status_pill)
        vehicleImageLabel = root.findViewById(R.id.vehicle_image_label)
        oilTypeImageLabel = root.findViewById(R.id.oil_type_image_label)
        locationMapView = root.findViewById(R.id.preview_location_map_view)
        streetAddressImageLabel = root.findViewById(R.id.location_image_label)
        distanceBetweenTextView = root.findViewById(R.id.distance_text_view)

        streetAddressImageLabel.imageType = ImageLabel.ImageType.LOCATION
        vehicleImageLabel.imageType = ImageLabel.ImageType.VEHICLE
        oilTypeImageLabel.imageType = ImageLabel.ImageType.OIL

        locationMapView.onCreate(savedInstanceState)

        locationMapView.getMapAsync(this)

        autoServiceDetailsViewModel = ViewModelProviders.of(this).get(AutoServiceDetailsViewModel::class.java)

        autoServiceDetailsViewModel.autoServiceId = autoServiceId

        autoServiceDetailsViewModel.autoServiceElement.observe(this, Observer<AutoServiceListElements> { autoService ->
            Log.v("tag autoservice", "autoservice" + autoService.mechanicUser.firstName)
            val date = autoService.autoService.scheduledDate
            if (date != null) {
                dateDisplay.configure(date)
            }
            mechanicNameTextView.text = autoService.mechanicUser.displayName()
            statusPillButton.text = autoService.autoService.status?.localizedString()

            vehicleImageLabel.text = autoService.vehicle.displayValue()
            streetAddressImageLabel.text = autoService.location.streetAddress
            val location = autoService.location.latLong
            this.autoServiceLocation = autoService.location.location
            updateDistanceToService()
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Service location")
            )
        })

//        autoServicesListViewModel = ViewModelProviders.of(this).get(AutoServicesListViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_autoservices_list, container, false)
//
//        autoServicesListViewModel.autoServices.observe(this, Observer<List<AutoServiceListElements>>{ autoServices ->
//            Log.d("log", "It done changed")
//
//            viewAdapter.notifyDataSetChanged()
//        })
//
//        viewManager = LinearLayoutManager(activity?.applicationContext)
//        viewAdapter = AutoServiceListAdapter(autoServicesListViewModel.autoServices)
//
//        recyclerView = root.findViewById<RecyclerView>(R.id.autoservice_recycler_view).apply {
//            // use this setting to improve performance if you know that changes
//            // in content do not change the layout size of the RecyclerView
//            setHasFixedSize(true)
//
//            // use a linear layout manager
//            layoutManager = viewManager
//
//            // specify an viewAdapter (see also next example)
//            adapter = viewAdapter
//        }

////        val textView: TextView = root.findViewById(R.id.text_home)
////        autoServicesListViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
//
//        autoServicesListViewModel.also {
//
//        }

//        val model: AutoServicesListViewModel by viewModels()
//        model.getUsers().observe(this, Observer<List<User>>{ users ->
//            // update UI
//        })

        return root
    }

    private fun updateDistanceToService() {
        val userLocation = this.userLocation
        val autoServiceLocation = this.autoServiceLocation
        if (userLocation != null && autoServiceLocation != null) {
            distanceBetweenTextView.text = localizedDistance(autoServiceLocation, userLocation)
        }
    }

    private fun localizedDistance(autoServiceLocation: Location, userLocation: Location): String? {
        val locale = resources.configuration.locales.get(0)
        if (locale == null) {
            return null
        }
        val measureFormat = MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.SHORT)
        var metersBetween = userLocation.distanceTo(autoServiceLocation)
        metersBetween = metersBetween.toBigDecimal().setScale(0, RoundingMode.HALF_DOWN).toFloat()
        val measure = Measure(metersToMiles(metersBetween), MeasureUnit.MILE)
        return measureFormat.format(measure)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Centered on lehi showing slc + provo
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(saltLakeAndProvo, 10f))
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                if (location == null) {
                    return@addOnSuccessListener
                }
                userLocation = location
                updateDistanceToService()
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(
                activity as AppCompatActivity, locationPermissionRequestCode,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }


    private fun metersToMiles(meters: Float): Float {
        return meters / 1609.34f
    }

}