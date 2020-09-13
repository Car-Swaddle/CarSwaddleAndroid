package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carswaddle.carswaddleandroid.Extensions.openSettingsToAppActions
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
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
import com.google.android.gms.maps.model.MarkerOptions
import java.math.RoundingMode


class AutoServiceDetailsFragment(val autoServiceId: String) : Fragment(), OnMapReadyCallback {

    private lateinit var dateDisplay: DateDisplayView
    private lateinit var mechanicNameTextView: TextView
    private lateinit var statusPillButton: Button
    private lateinit var vehicleImageLabel: ImageLabel
    private lateinit var oilTypeImageLabel: ImageLabel
    private lateinit var streetAddressImageLabel: ImageLabel
    private lateinit var locationMapView: MapView
    private lateinit var distanceBetweenTextView: TextView
    private lateinit var chatButton: Button
    private lateinit var phoneButton: Button

    private lateinit var notesView: NotesView

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
        chatButton = root.findViewById(R.id.chatButton)
        phoneButton = root.findViewById(R.id.phoneButton)
        notesView = root.findViewById(R.id.notesView)

        notesView.notesDidChange = {
            autoServiceDetailsViewModel.updateNotes(it ?: "") { error, autoServiceId ->
                Log.w("car swaddle android", "returned")
            }
        }

        chatButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val phoneNumber = autoServiceDetailsViewModel.autoServiceElement.value?.mechanicUser?.phoneNumber ?: return
                val intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null))
                startActivity(intent)
            }
        })

        phoneButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val phoneNumber = autoServiceDetailsViewModel.autoServiceElement.value?.mechanicUser?.phoneNumber ?: return
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
                startActivity(intent)
            }
        })

        streetAddressImageLabel.imageType = ImageLabel.ImageType.LOCATION
        vehicleImageLabel.imageType = ImageLabel.ImageType.VEHICLE
        oilTypeImageLabel.imageType = ImageLabel.ImageType.OIL

        locationMapView.onCreate(savedInstanceState)

        locationMapView.getMapAsync(this)

        autoServiceDetailsViewModel = ViewModelProviders.of(this).get(AutoServiceDetailsViewModel::class.java)

        autoServiceDetailsViewModel.autoServiceId = autoServiceId

        autoServiceDetailsViewModel.autoServiceElement.observe(viewLifecycleOwner, Observer<AutoServiceListElements> { autoService ->
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
            notesView.notesText = autoService.autoService.notes
        })

        autoServiceDetailsViewModel.oilChange.observe(viewLifecycleOwner, Observer<OilChange> { oilChange ->
            oilTypeImageLabel.text = oilChange.oilType.localizedString()
        })

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
        val locale = resources.configuration.locales.get(0) ?: return null
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
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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