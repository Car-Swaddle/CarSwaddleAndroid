package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.Location
import com.carswaddle.carswaddleandroid.data.location.LocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.launch
import java.lang.Exception


class AutoServiceDetailsViewModel(application: Application) : AndroidViewModel(application) {


    var autoServiceId: String? = null
    set(value) {
        field = value
        loadAutoService()
    }

    private val autoServiceRepo: AutoServiceRepository
    private val locationRepo: LocationRepository
    private val mechanicRepo: MechanicRepository
    private val userRepo: UserRepository
    private val vehicleRepo: VehicleRepository

    init {
        val db = AppDatabase.getDatabase(application)
        autoServiceRepo = AutoServiceRepository(db.autoServiceDao())
        locationRepo = LocationRepository(db.locationDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
        userRepo = UserRepository(db.userDao())
        vehicleRepo = VehicleRepository(db.vehicleDao())

        loadAutoService()
    }

    private val _autoServiceElement = MutableLiveData<AutoServiceListElements>()

    private fun loadAutoService() {

        val localAutoServiceId = this.autoServiceId
        if (localAutoServiceId == null) {

        } else {

            autoServiceRepo.getAutoService(localAutoServiceId, getApplication(), { error, autoServiceId ->
                if (error == null && autoServiceId != null) {
                    viewModelScope.launch {
                        _autoServiceElement.value = fetchAutoServiceListElements(autoServiceId)
                    }
                } else {

                }
            })
        }

//        autoServiceRepo.getAutoServices(100, 0, getApplication(), listOf<String>(), listOf("scheduled", "canceled", "inProgress")) { error, autoServiceIds ->
//
//            viewModelScope.launch {
//                if (autoServiceIds != null) {
//                    var autoServiceElements: MutableList<AutoServiceListElements> = ArrayList()
//                    for (id in autoServiceIds) {
//                        fetchAutoServiceListElements(id)?.let {
//                            autoServiceElements.add(it)
//                        }
//                    }
//                    _autoServices.value = autoServiceElements
//                } else {
//
//                }
//            }
//        }
    }

    val autoServiceElement: LiveData<AutoServiceListElements>
        get() = _autoServiceElement

    suspend private fun fetchAutoServiceListElements(autoServiceId: String): AutoServiceListElements? {
        try {
            val autoService = autoServiceRepo.getAutoService(autoServiceId)
            val vehicleId = autoService?.vehicleId
            val locationId = autoService?.locationId
            if (autoService == null || vehicleId == null || locationId == null) {
                return null
            }
            val mechanic = mechanicRepo.getMechanic(autoService.mechanicId)
            val vehicle = vehicleRepo.getVehicle(vehicleId)
            val location = locationRepo.getLocation(locationId)
            val mechanicUser = userRepo.getUser(mechanic?.userId ?: "")

            if (mechanic == null || vehicle == null || location == null || mechanicUser == null) {
                return null
            }
            return AutoServiceListElements(autoService, mechanic, vehicle, location, mechanicUser)
        } catch (e: Exception) {
            print(e)
            return null
        }
    }

}

