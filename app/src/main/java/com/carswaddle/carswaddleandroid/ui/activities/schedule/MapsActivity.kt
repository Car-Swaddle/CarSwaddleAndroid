package com.carswaddle.carswaddleandroid.ui.activities.schedule

//import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan as TimeSlot
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.Extensions.safeFirst
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.carswaddleandroid.services.serviceModels.Price
import com.carswaddle.carswaddleandroid.ui.activities.schedule.details.SelectDetailsFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import java.util.*


class MapsActivity : AppCompatActivity(), LocationFragment.OnLocationSelectedListener, MechanicFragment.OnConfirmListener,
    SelectDetailsFragment.OnPriceUpdatedListener {

    private var location: LatLng? = null
    private lateinit var progressFragment: ProgressFragment
    private lateinit var mechanicFragment: MechanicFragment
    private var selectDetailsFragment: SelectDetailsFragment? = null
    private lateinit var priceFragment: PriceFragment
    
    private var streetAddress: String? = null

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
        
        val streetAddress = this.streetAddress(latLng)
        this.streetAddress = streetAddress

        val mechanicFragment = MechanicFragment()
        mechanicFragment.point = Point(latLng, streetAddress)
        mechanicFragment.setOnConfirmCallbackListener(this)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, mechanicFragment)
            .addToBackStack("Mechanic")
            .commit()
    }
    
    fun streetAddress(latLng: LatLng): String? {
        var geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            latLng.latitude,
            latLng.longitude,
            1
        )
        return addresses.safeFirst()?.getAddressLine(0)
    }

    override fun onPriceUpdated(price: Price) {
        priceFragment.price = price
    }

    override fun onConfirm(mechanicId: String, date: Date) {
        val l = location
        val s = streetAddress
        if (l == null || s == null) {
            return
        }

        val point = Point(l.latitude, l.longitude, s)
        val details = SelectDetailsFragment(point, mechanicId, date)
        details.listener = this

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, details)
            .replace(R.id.bottom_fragment_container, priceFragment, "price")
            .addToBackStack("Details")
            .commit()
    }


}
