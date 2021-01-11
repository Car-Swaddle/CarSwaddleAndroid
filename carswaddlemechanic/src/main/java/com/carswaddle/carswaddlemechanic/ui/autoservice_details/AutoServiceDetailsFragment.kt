package com.carswaddle.carswaddlemechanic.ui.autoservice_details

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.location.Location
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carswaddle.carswaddleandroid.ImageLabel
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.CreateReview
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.application.CarSwaddleMechanicApp
import com.carswaddle.carswaddlemechanic.extensions.updateMapStyle
import com.carswaddle.carswaddlemechanic.ui.common.AutoserviceStatusView
import com.carswaddle.carswaddlemechanic.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

public val saltLakeAndProvo = LatLng(40.4456955, -111.8971674)


class AutoServiceDetailsFragment() : Fragment(), OnMapReadyCallback {

    private lateinit var mechanicNameTextView: TextView
    private lateinit var vehicleImageLabel: ImageLabel
    private lateinit var oilTypeImageLabel: ImageLabel
    private lateinit var streetAddressImageLabel: ImageLabel
    private lateinit var locationMapView: MapView
    private lateinit var distanceBetweenTextView: TextView
    private lateinit var chatImageView: ImageView
    private lateinit var phoneImageView: ImageView
    private lateinit var timeImageLabel: ImageLabel
    private lateinit var statusBannerView: TextView
    private lateinit var changeStatusButton: ProgressButton
    private lateinit var cancelTableRow: TableRow
    private lateinit var statusBannerBackgroundView: View
    
    private lateinit var statusView: AutoserviceStatusView
    
    private lateinit var autoServiceId: String

    private lateinit var cancelAutoServiceButton: Button

    private lateinit var notesView: NotesView

    private var userLocation: Location? = null

    private var autoServiceLocation: Location? = null

    private val locationPermissionRequestCode = 1
    private val zoomLevel = 18.0f

    private lateinit var map: GoogleMap
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private lateinit var autoServiceDetailsViewModel: AutoServiceDetailsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_autoservice_details, container, false)

        this.autoServiceId = arguments?.getString("autoServiceId") ?: ""

        activity?.let {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }

        mechanicNameTextView = root.findViewById(R.id.mechanic_name_text_view)
        vehicleImageLabel = root.findViewById(R.id.vehicle_image_label)
        oilTypeImageLabel = root.findViewById(R.id.oil_type_image_label)
        locationMapView = root.findViewById(R.id.preview_location_map_view)
        streetAddressImageLabel = root.findViewById(R.id.location_image_label)
        distanceBetweenTextView = root.findViewById(R.id.distance_text_view)
        chatImageView = root.findViewById(R.id.chatImageView)
        phoneImageView = root.findViewById(R.id.phoneImageView)
        notesView = root.findViewById(R.id.notesView)
        cancelAutoServiceButton = root.findViewById(R.id.cancelAutoService)
        timeImageLabel = root.findViewById(R.id.time_image_label)
        statusBannerView = root.findViewById(R.id.statusBannerView)
        statusBannerBackgroundView = root.findViewById(R.id.statusBannerBackgroundView)
        changeStatusButton = root.findViewById(R.id.changeStatusButton)
        cancelTableRow = root.findViewById(R.id.cancelTableRow)
        statusView = root.findViewById(R.id.statusView)

        vehicleImageLabel.text = "--"
        oilTypeImageLabel.text = "--"
        timeImageLabel.text = "--"
        streetAddressImageLabel.text = "--"
        mechanicNameTextView.text = "--"
//        statusBannerView.backgroundTintList = ColorStateList.valueOf(defaultStatusColor())

        notesView.notesDidChange = {
            autoServiceDetailsViewModel.updateNotes(it ?: "", requireContext()) { error, autoServiceId ->
                Log.w("car swaddle android", "returned")
            }
        }

        chatImageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val phoneNumber =
                    autoServiceDetailsViewModel.autoServiceElement.value?.mechanicUser?.phoneNumber
                        ?: return
                val intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null))
                startActivity(intent)
            }
        })

        phoneImageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val phoneNumber =
                    autoServiceDetailsViewModel.autoServiceElement.value?.mechanicUser?.phoneNumber
                        ?: return
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
                startActivity(intent)
            }
        })

        streetAddressImageLabel.imageType = ImageLabel.ImageType.LOCATION
        vehicleImageLabel.imageType = ImageLabel.ImageType.VEHICLE
        oilTypeImageLabel.imageType = ImageLabel.ImageType.OIL
        timeImageLabel.imageType = ImageLabel.ImageType.TIME


//        locationMapView.onCreate(savedInstanceState)

//        locationMapView.getMapAsync(this)

        autoServiceDetailsViewModel =
            ViewModelProvider(requireActivity()).get(AutoServiceDetailsViewModel::class.java)

        autoServiceDetailsViewModel.autoServiceId = autoServiceId


        autoServiceDetailsViewModel.autoServiceElement.observe(viewLifecycleOwner) { autoService ->
            Log.v("tag autoservice", "autoservice" + autoService.mechanicUser.firstName)
            val date = autoService.autoService.scheduledDate
            if (date != null) {
//                dateDisplay.configure(date)
                // TODO("Configure Date")
            }
            mechanicNameTextView.text = autoService.user?.displayName()
            statusBannerView.text = autoService.autoService.status?.localizedString()

            configureUpdateStatusButton(autoService.autoService.status ?: AutoServiceStatus.scheduled)

            val d = autoService.autoService.scheduledDate
            if (d != null) {
                val localDateTime = LocalDateTime.ofInstant(d.toInstant(), d.timeZone.toZoneId())
                timeImageLabel.text = timeFormatter.format(localDateTime)
            }

            val status = autoService.autoService.status
            if (status != null) {
                statusView.autoServiceStatus = status
                statusBannerBackgroundView.background = statusBackground(status)
                
                if (status == AutoServiceStatus.scheduled) {
                    cancelAutoServiceButton.visibility = View.VISIBLE
                    cancelTableRow.visibility = View.VISIBLE
                } else {
                    cancelAutoServiceButton.visibility = View.GONE
                    cancelTableRow.visibility = View.GONE
                }
            }

            vehicleImageLabel.text = autoService.vehicle.displayValue()
            streetAddressImageLabel.text = autoService.location.streetAddress ?: ""
            val location = autoService.location.latLong
            this.autoServiceLocation = autoService.location.location
            updateDistanceToService()
            if (this::map.isInitialized) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
                map.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("Service location")
                )
            }
            notesView.notesText = autoService.autoService.notes
        }

        autoServiceDetailsViewModel.oilChange.observe(viewLifecycleOwner) { oilChange ->
            oilTypeImageLabel.text = oilChange.oilType.localizedString()
        }

        cancelAutoServiceButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val autoService = autoServiceDetailsViewModel.autoServiceElement.value
            val d = autoService?.autoService?.scheduledDate
            if (d == null) {
                return@setOnClickListener
            }
            val message = dialogMessage(d)
            val title = getString(R.string.cancel_service_sure)
            dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel_auto_service), { dialog, id ->
                    autoServiceDetailsViewModel.cancelAutoService(requireContext()) { error, autoServiceId -> }
                })
                .setNeutralButton(getString(R.string.dismiss)) { dialog, id -> }
            val alert = dialogBuilder.create()
            alert.setTitle(title)
            alert.show()
        }
        
        changeStatusButton.button.setOnClickListener { 
            val s = autoServiceDetailsViewModel.autoServiceElement.value?.autoService?.status
            if (s == null) {
                return@setOnClickListener
            }
            
            val nextStatus = changeToStatusForStatus(s)
            if (nextStatus == null) {
                return@setOnClickListener
            }
            changeStatusButton.isLoading = true
            autoServiceDetailsViewModel.setAutoServiceStatus(nextStatus, requireContext()) { error, autoServiceId ->
                changeStatusButton.isLoading = false
                if (error != null) {
                    // TODO("Show error message")
                }
            }
        }

        return root
    }

    private fun dialogMessage(scheduledDate: Calendar): String {
        if (userWillReceiveRefund(scheduledDate)) {
            return getString(R.string.funds_refunded)
        } else {
            return getString(R.string.no_funds_refund)
        }
    }

    private fun userWillReceiveRefund(date: Calendar): Boolean {
        val now = Calendar.getInstance()
        val diff: Long = date.timeInMillis - now.timeInMillis
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return hours > 24
    }

    private fun updateDistanceToService() {
        val userLocation = this.userLocation
        val autoServiceLocation = this.autoServiceLocation
        if (userLocation != null && autoServiceLocation != null) {
            distanceBetweenTextView.text = localizedDistance(autoServiceLocation, userLocation)
        }
    }

    private fun localizedDistance(autoServiceLocation: Location, userLocation: Location): String? {
        var metersBetween = userLocation.distanceTo(autoServiceLocation)
        metersBetween = metersBetween.toBigDecimal().setScale(0, RoundingMode.HALF_DOWN).toFloat()
        val milesBetween = metersToMiles(metersBetween)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val locale = resources.configuration.locales.get(0) ?: return null
            val measureFormat = MeasureFormat.getInstance(locale, MeasureFormat.FormatWidth.SHORT)
            val measure = Measure(milesBetween, MeasureUnit.MILE)
            return measureFormat.format(measure)
        } else {
            return String.format("%.1f miles", milesBetween)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        googleMap.getUiSettings().setScrollGesturesEnabled(false)

        googleMap.updateMapStyle(requireContext())

        // Centered on lehi showing slc + provo
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(saltLakeAndProvo, 10f))
        enableMyLocation()
    }

    private fun updateMapStyle() {
        try {
            val mode =
                context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
            var mapStyle = R.raw.standard_map
            when (mode) {
                Configuration.UI_MODE_NIGHT_YES -> mapStyle = R.raw.night_mode_map
                Configuration.UI_MODE_NIGHT_NO -> {
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                }
            }
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), mapStyle))
        } catch (e: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", e)
        }
    }

    private fun statusColor(status: AutoServiceStatus): Int {
        val colorId = when (status) {
            AutoServiceStatus.scheduled -> R.color.statusColorScheduled
            AutoServiceStatus.canceled -> R.color.statusColorCanceled
            AutoServiceStatus.inProgress -> R.color.statusColorInProgress
            AutoServiceStatus.completed -> R.color.statusColorCompleted
        }
        val t = context?.theme ?: return 0
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorId, t)
        } else {
            resources.getColor(colorId)
        }
    }

    private fun statusBackground(status: AutoServiceStatus): Drawable {
        val drawableResource = when (status) {
            AutoServiceStatus.scheduled -> R.drawable.right_round_scheduled
            AutoServiceStatus.canceled -> R.drawable.right_round_canceled
            AutoServiceStatus.inProgress -> R.drawable.right_round_in_progress
            AutoServiceStatus.completed -> R.drawable.right_round_complete
        }
        return resources.getDrawable(drawableResource, null)
    }

    private fun defaultStatusColor(): Int {
        val t = context?.theme ?: return 0
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(R.color.icon, t)
        } else {
            resources.getColor(R.color.icon)
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
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

    private fun updateServerWithReview(rating: Float, text: String) {
        autoServiceDetailsViewModel.createReview(
            CreateReview(
                rating,
                text
            )
        ) { error, autoServiceId ->

        }
    }

    private fun metersToMiles(meters: Float): Float {
        return meters / 1609.34f
    }

    // See https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/gms/java/com/example/kotlindemos/RawMapViewDemoActivity.kt
    // Need to forward all lifecycle events to map view
//    override fun onResume() {
//        super.onResume()
//        locationMapView.onResume()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        locationMapView.onStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        locationMapView.onStop()
//    }
//
//    override fun onPause() {
//        locationMapView.onPause()
//        super.onPause()
//    }
//
//    override fun onDestroy() {
//        locationMapView.onDestroy()
//        super.onDestroy()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        locationMapView.onLowMemory()
//    }

    private fun configureUpdateStatusButton(status: AutoServiceStatus) {
        changeStatusButton.displayText = buttonStatusTitle(status) ?: ""
        changeStatusButton.visibility = changeStatusButtonVisibility(status)
    }

    private fun statusColorStateList(status: AutoServiceStatus): ColorStateList? {
        val colorId = when (status) {
            AutoServiceStatus.scheduled -> R.color.statusColorScheduled
            AutoServiceStatus.canceled -> R.color.statusColorCanceled
            AutoServiceStatus.inProgress -> R.color.statusColorInProgress
            AutoServiceStatus.completed -> R.color.statusColorCompleted
        }
        return ContextCompat.getColorStateList(CarSwaddleMechanicApp.applicationContext, colorId)
    }

    private fun buttonStatusTitle(status: AutoServiceStatus): String? {
        return when (status) {
            AutoServiceStatus.scheduled -> "Start Service"
            AutoServiceStatus.inProgress -> "Complete Service"
            AutoServiceStatus.canceled -> null
            AutoServiceStatus.completed -> null
        }
    }

    private fun changeToStatusForStatus(status: AutoServiceStatus): AutoServiceStatus? {
        return when (status) {
            AutoServiceStatus.scheduled -> AutoServiceStatus.inProgress
            AutoServiceStatus.inProgress -> AutoServiceStatus.completed
            AutoServiceStatus.canceled -> null
            AutoServiceStatus.completed -> null
        }
    }

    private fun changeStatusButtonVisibility(status: AutoServiceStatus): Int {
        return when (status) {
            AutoServiceStatus.scheduled -> View.VISIBLE
            AutoServiceStatus.inProgress -> View.VISIBLE
            AutoServiceStatus.canceled -> View.GONE
            AutoServiceStatus.completed -> View.GONE
        }
    }

    companion object {
        private val timeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("h:mm ")
            .appendText(ChronoField.AMPM_OF_DAY, mapOf(0L to "am", 1L to "pm"))
            .appendPattern(" - MMM d, yyyy")
            .toFormatter(Locale.US)
    }

}
