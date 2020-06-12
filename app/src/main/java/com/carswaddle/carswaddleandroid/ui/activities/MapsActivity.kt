package com.carswaddle.carswaddleandroid.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.util.PermissionUtils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var map: GoogleMap

    private val locationPermissionRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))

//        val client = Places.createClient(this);
//        val task = client.findAutocompletePredictions(FindAutocompletePredictionsRequest.newInstance("7277 n clear sky ln"))
//        task.addOnCompleteListener {
//            it.result
//        }

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment: AutocompleteSupportFragment? =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        // Specify the types of place data to return.
        autocompleteFragment?.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.ADDRESS_COMPONENTS))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(
                    "autocomplete",
                    "Place: " + place.getName().toString() + ", " + place.getId()
                )
                val latLng = place.latLng ?: return
                // 15 is street level, 20 is building level
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
//                map.cameraPosition.target
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("autocomplete", "An error occurred: $status")
            }
        })

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true

//            val placesClient = Places.createClient(this)
//            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//            val request = FindCurrentPlaceRequest.newInstance(placeFields)
//            val placeResponse = placesClient.findCurrentPlace(request)
//            placeResponse.addOnCompleteListener { task: Task<FindCurrentPlaceResponse?> ->
//                if (task.isSuccessful) {
//                    val response = task.result
//                    for (placeLikelihood in response!!.placeLikelihoods) {
//                        Log.i(
//                            "test", String.format(
//                                "Place '%s' has likelihood: %f",
//                                placeLikelihood.place.name,
//                                placeLikelihood.likelihood
//                            )
//                        )
//                    }
//                } else {
//                    val exception = task.exception
//                    if (exception is ApiException) {
//                        val apiException = exception as ApiException
//                        Log.e(
//                            "test",
//                            "Place not found: " + apiException.statusCode
//                        )
//                    }
//                }
//            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(
                this, locationPermissionRequestCode,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }
}
