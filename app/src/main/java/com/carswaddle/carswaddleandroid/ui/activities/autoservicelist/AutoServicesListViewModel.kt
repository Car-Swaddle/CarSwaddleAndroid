package com.carswaddle.carswaddleandroid.activities.ui.home

import android.app.Application
import androidx.lifecycle.*
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.*
import java.lang.Exception

class AutoServicesListViewModel(application: Application) : AndroidViewModel(application) {

    private val autoServiceRepo: AutoServiceRepository
    private val locationRepo: AutoServiceLocationRepository
    private val mechanicRepo: MechanicRepository
    private val userRepo: UserRepository
    private val vehicleRepo: VehicleRepository
    private val serviceEntityRepo: ServiceEntityRepository
    private val oilChangeRepo: OilChangeRepository

    init {
        val db = AppDatabase.getDatabase(application)
        autoServiceRepo = AutoServiceRepository(db.autoServiceDao())
        locationRepo = AutoServiceLocationRepository(db.locationDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
        userRepo = UserRepository(db.userDao())
        vehicleRepo = VehicleRepository(db.vehicleDao())
        serviceEntityRepo = ServiceEntityRepository(db.serviceEntityDao())
        oilChangeRepo = OilChangeRepository(db.oilChangeDao())

        loadAutoServices()
    }

    private val _autoServices = MutableLiveData<List<AutoServiceListElements>>()

    private fun loadAutoServices() {
        autoServiceRepo.getAutoServices(
            100,
            0,
            getApplication(),
            listOf<String>(),
            listOf("scheduled", "canceled", "inProgress")
        ) { error, autoServiceIds ->

            viewModelScope.launch {
                if (autoServiceIds != null) {
                    var autoServiceElements: MutableList<AutoServiceListElements> = ArrayList()
                    for (id in autoServiceIds) {
                        fetchAutoServiceListElements(id)?.let {
                            autoServiceElements.add(it)
                        }
                    }
                    _autoServices.value = autoServiceElements
                } else {

                }
            }
        }
    }

    val autoServices: LiveData<List<AutoServiceListElements>>
        get() = _autoServices

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
            val serviceEntities = serviceEntityRepo.getServiceEntities(autoServiceId)

            if (mechanic == null || vehicle == null || location == null || mechanicUser == null) {
                return null
            }

            return AutoServiceListElements(
                autoService,
                mechanic,
                vehicle,
                location,
                mechanicUser,
                serviceEntities
            )
        } catch (e: Exception) {
            print(e)
            return null
        }
    }

}