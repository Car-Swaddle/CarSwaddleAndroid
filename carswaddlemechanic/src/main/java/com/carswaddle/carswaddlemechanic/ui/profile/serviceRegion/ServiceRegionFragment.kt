package com.carswaddle.carswaddlemechanic.ui.profile.serviceRegion

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.MotionEvent.INVALID_POINTER_ID
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateRegion
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.extensions.updateMapStyle
import com.carswaddle.carswaddlemechanic.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val saltLakeAndProvo = LatLng(40.4456955, -111.8971674)

class ServiceRegionFragment : Fragment(), OnMapReadyCallback {

    private lateinit var regionView: View

    private lateinit var viewModel: ServiceRegionViewModel

    private lateinit var regionMap: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    private lateinit var saveButton: ProgressButton

    private val locationPermissionRequestCode = 1
    private val zoomLevel = 10.0f

    private var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ServiceRegionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_region, container, false)

        rootView = root

        saveButton = root.findViewById(R.id.saveRegionProgressButton)
        
        regionMap = childFragmentManager.findFragmentById(R.id.regionMap) as SupportMapFragment
        regionMap.getMapAsync(this)

        regionView = root.findViewById(R.id.regionView)

        regionView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    updateView(event)
                }
                return true
            }
        })

        saveButton.button.setOnClickListener {
            saveRegion()
        }
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        viewModel.region.observe(viewLifecycleOwner) {
            if (it != null) {
                val camUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude),
                    zoomLevel
                )
                map.animateCamera(camUpdate)
            }
        }

        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.updateMapStyle(requireContext())
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(saltLakeAndProvo, zoomLevel))
        enableMyLocation()
    }


    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null || viewModel.region.value != null) {
                    return@addOnSuccessListener
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), zoomLevel))
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(
                activity as AppCompatActivity, locationPermissionRequestCode,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }


    private var touchPositionXOffset: Float = 0.0f
    private var touchPositionYOffset: Float = 0.0f
    
    private var touchPercentage: Double = 0.0


    private fun updateView(ev: MotionEvent) {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                touchPositionYOffset = ev.rawY - regionView.globalVisibleRect().y()
                touchPositionXOffset = ev.rawX - regionView.globalVisibleRect().x()

                val radius = distance(ev.rawX.toDouble(), regionView.globalCenterX().toDouble(), ev.rawY.toDouble(), regionView.globalCenterY().toDouble())
                val fullRadius = regionView.width / 2
                touchPercentage = fullRadius / radius
            }
            MotionEvent.ACTION_MOVE -> {
                val radius = distance(ev.rawX.toDouble(), regionView.globalCenterX().toDouble(), ev.rawY.toDouble(), regionView.globalCenterY().toDouble())
                val newWidth = abs(2*(radius*touchPercentage)).toInt()
                
                var adjustedWidth = max(newWidth, 200)
                val d = rootView
                if (d != null) {
                    adjustedWidth = min(adjustedWidth, d.width - 45)
                }
                
                val oldParams = regionView.layoutParams
                
                oldParams.width = adjustedWidth
                oldParams.height = adjustedWidth
                
                regionView.layoutParams = oldParams
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { 
                touchPercentage = 0.0
                touchPositionYOffset = 0.0f
                touchPositionXOffset = 0.0f
            }
            MotionEvent.ACTION_POINTER_UP -> { }
        }
    }
    
    private fun saveRegion() {
        val centerPoint = Point(regionView.centerX().toInt(), regionView.centerY().toInt())
        val startXPoint = Point(regionView.x.toInt(), regionView.y.toInt())
        val centerScreenLocation = map.projection.fromScreenLocation(centerPoint)
        val startScreenLocation = map.projection.fromScreenLocation(startXPoint)
        
        
        var results: FloatArray = FloatArray(3)
        Location.distanceBetween(centerScreenLocation.latitude, centerScreenLocation.longitude, startScreenLocation.latitude, startScreenLocation.longitude, results)
        val distance = results[0]  // distance is in meters
        
        
        Log.d("dist", "distance: $distance")
        
        saveButton.isLoading = true
        
        val u = UpdateRegion(centerScreenLocation.latitude, centerScreenLocation.longitude, distance.toDouble())
        viewModel.updateRegion(u, requireContext()) {
            if (it == null) {
                requireActivity().runOnUiThread {
                    finishSuccessfully()
                }
            }
        }
    }

    private fun finishSuccessfully() {
        saveButton.isLoading = false
        findNavController().popBackStack()
        Toast.makeText(requireContext(), "Car Swaddle successfully saved your region.", Toast.LENGTH_SHORT).show()
    }
    
    private fun distance(x1: Double, x2: Double, y1: Double, y2: Double): Double {
        val xs = (x1-x2)
        val ys = (y1-y2)
        return Math.sqrt((xs*xs) + (ys*ys))
    }
    
}


fun View.centerX(): Float {
    return x + (width/2)
}

fun View.centerY(): Float {
    return y + (height/2)
}

fun View.globalCenterX(): Float {
    return globalVisibleRect().x().toFloat() + (width/2)
}

fun View.globalCenterY(): Float {
    return globalVisibleRect().y().toFloat() + (height/2)
}

fun View.globalXOffset(): Float {
    return globalVisibleRect().x() - x
}

fun View.globalYOffset(): Float {
    return globalVisibleRect().y() - y
}

fun View.globalVisibleRect(): Rect {
    var r = Rect()
    this.getGlobalVisibleRect(r)
    return r
}

fun Rect.x(): Int {
    return this.left
}

fun Rect.y(): Int {
    return this.top
}

fun View.relativeX(): Int {
    val offsetViewBounds = Rect()
    (this.parent as? ViewGroup)?.offsetDescendantRectToMyCoords(this, offsetViewBounds)
    return offsetViewBounds.left
}

fun View.relativeY(): Int {
    val offsetViewBounds = Rect()
    (this.parent as? ViewGroup)?.offsetDescendantRectToMyCoords(this, offsetViewBounds)
    return offsetViewBounds.bottom
}
