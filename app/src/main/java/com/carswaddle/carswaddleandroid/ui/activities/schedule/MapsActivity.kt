package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
//import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan as TimeSlot
import com.carswaddle.carswaddleandroid.util.PermissionUtils
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.carswaddle.carswaddleandroid.ui.activities.schedule.details.SelectDetailsFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places


class MapsActivity : AppCompatActivity(), LocationFragment.OnLocationSelectedListener, MechanicFragment.OnConfirmListener {

    private var location: LatLng? = null
    private lateinit var progressFragment: ProgressFragment
    private lateinit var mechanicFragment: MechanicFragment
    private var selectDetailsFragment: SelectDetailsFragment? = null
    private lateinit var priceFragment: PriceFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val locationFragment = LocationFragment()
        locationFragment.setOnLocationSelectedListener(this)

        progressFragment = ProgressFragment()
        priceFragment = PriceFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, locationFragment)
            .add(R.id.bottom_fragment_container, progressFragment)
            .commit();

        // Update progress when back button pressed
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.fragments.stream().anyMatch { f -> f is MechanicFragment }) {
                progressFragment.stepNumber = 2
            } else {
                progressFragment.stepNumber = 1
            }
        }
    }

    override fun onLocationSelected(latLng: LatLng) {
        location = latLng
        progressFragment.stepNumber = 2

        val mechanicFragment = MechanicFragment(Point(latLng))
        mechanicFragment.setOnConfirmCallbackListener(this)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, mechanicFragment)
            .addToBackStack("Mechanic")
            .commit()
    }

    override fun onConfirm(mechanicId: String, timeSlot: TemplateTimeSpan) {
        val l = location
        if (l == null) {
            return 
        }
        
        val point = Point(l.latitude, l.longitude)
        val details = SelectDetailsFragment(point, mechanicId)
        
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, details)
            .replace(R.id.bottom_fragment_container, priceFragment)
            .addToBackStack("Details")
            .commit();
    }


}
