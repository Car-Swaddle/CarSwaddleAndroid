package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.util.PermissionUtils
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class MapsActivity : AppCompatActivity(), LocationFragment.OnLocationSelectedListener {

    private var location: LatLng? = null
    private lateinit var progressFragment: ProgressFragment
    private lateinit var mechanicFragment: MechanicFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val locationFragment = LocationFragment()
        locationFragment.setOnLocationSelectedListener(this)
        progressFragment = ProgressFragment()
        mechanicFragment = MechanicFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, locationFragment)
            .add(R.id.bottom_fragment_container, progressFragment)
            .commit();
    }

    override fun onLocationSelected(latLng: LatLng) {
        location = latLng
        progressFragment.stepNumber = 2
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, mechanicFragment)
            .addToBackStack("Mechanic")
            .commit()
    }

}
