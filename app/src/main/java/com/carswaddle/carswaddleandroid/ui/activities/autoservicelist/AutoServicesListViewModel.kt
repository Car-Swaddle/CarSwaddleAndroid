package com.carswaddle.carswaddleandroid.activities.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.location.Location
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.LocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescriptionRepository
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AutoServicesListViewModel(application: Application) : AndroidViewModel(application) {

    init {
        loadAutoServices()
    }

    private val autoServiceRepo: AutoServiceRepository by lazy {
        val autoServiceDao = AppDatabase.getDatabase(application).autoServiceDao()
        AutoServiceRepository(autoServiceDao)
    }

    private val locationRepo: LocationRepository by lazy {
        val dao = AppDatabase.getDatabase(application).locationDao()
        LocationRepository(dao)
    }

    private val mechanicRepo: MechanicRepository by lazy {
        val dao = AppDatabase.getDatabase(application).mechanicDao()
        MechanicRepository(dao)
    }

    private val vehicleRepo: VehicleRepository by lazy {
        val dao = AppDatabase.getDatabase(application).vehicleDao()
        VehicleRepository(dao)
    }

    private val vehicleDescriptionRepo: VehicleDescriptionRepository by lazy {
        val dao = AppDatabase.getDatabase(application).vehicleDescriptionDao()
        VehicleDescriptionRepository(dao)
    }

    private val userRepo: UserRepository by lazy {
        val dao = AppDatabase.getDatabase(application).userDao()
        UserRepository(dao)
    }

    private val _autoServices = MutableLiveData<List<AutoServiceListElements>>()

    private fun loadAutoServices() {
        autoServiceRepo.getAutoServices(100, 0, getApplication(), listOf<String>()) { error, autoServiceIds ->
            GlobalScope.async {
                if (autoServiceIds != null) {
                    var autoServiceElements: MutableList<AutoServiceListElements> = ArrayList()
                    for (id in autoServiceIds) {
                        val elements = fetchAutoServiceListElements(id)
                        elements?.let {
                            autoServiceElements.add(it)
                        }
                    }
                    _autoServices.value = autoServiceElements
                } else {
                    // TODO: do something!
                }
            }
        }
    }

    val autoServices: LiveData<List<AutoServiceListElements>>
        get() = _autoServices

//    fun getAutoServices(): LiveData<List<AutoService>> {
//        return _autoServices
//    }

//    var autoServices: LiveData<Array<AutoService>> = LiveData<Array<AutoService>>()

//    var autoServices: LiveData<Array<com.carswaddle.carswaddleandroid.data.autoservice.AutoService>>

    suspend private fun fetchAutoServiceListElements(autoServiceId: String): AutoServiceListElements? {
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
    }

}

data class AutoServiceListElements(
    val autoService: AutoService,
    val mechanic: Mechanic,
    val vehicle: Vehicle,
    val location: Location,
    val mechanicUser: User
)


/**
 *
 *
enum Status: String {
case scheduled
case canceled
case inProgress
case completed

public var localizedString: String {
switch self {
case .canceled: return NSLocalizedString("canceled", comment: "auto service status")
case .inProgress: return NSLocalizedString("in progress", comment: "auto service status")
case .completed: return NSLocalizedString("completed", comment: "auto service status")
case .scheduled: return NSLocalizedString("scheduled", comment: "auto service status")
}
}

}
 */

