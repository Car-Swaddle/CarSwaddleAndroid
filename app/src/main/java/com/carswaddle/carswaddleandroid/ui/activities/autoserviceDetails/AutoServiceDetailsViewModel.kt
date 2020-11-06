package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntity
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
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

        loadAutoService()
    }

    val oilChange: LiveData<OilChange>
        get() = _oilChange

    private val _oilChange = MutableLiveData<OilChange>()

    val autoServiceElement: LiveData<AutoServiceListElements>
        get() = _autoServiceElement

    private val _autoServiceElement = MutableLiveData<AutoServiceListElements>()

    private fun loadAutoService() {

        val localAutoServiceId = this.autoServiceId
        if (localAutoServiceId == null) {

        } else {
            autoServiceRepo.getAutoService(localAutoServiceId, getApplication(), {
                viewModelScope.launch {
                    _autoServiceElement.value = fetchAutoServiceListElements(it)
                }
            }, { error, autoServiceId ->
                if (error == null && autoServiceId != null) {
                    viewModelScope.launch {
                        _autoServiceElement.value = fetchAutoServiceListElements(autoServiceId)
                        autoServiceElement.value?.serviceEntities?.let {
                            if (it.size > 0) {
                                val service = it.get(0)
                                if (service != null) {
                                    _oilChange.value = oilChangeRepo.getOilChange(service.oilChangeID)
                                }
                            }
                        }
                    }
                } else {

                }
            })
        }
    }



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
            return AutoServiceListElements(autoService, mechanic, vehicle, location, mechanicUser, serviceEntities)
        } catch (e: Exception) {
            print(e)
            return null
        }
    }



//    suspend private fun fetchAutoServiceListElements(autoServiceId: String): AutoServiceListElements? {
//        try {
//            val autoService = autoServiceRepo.getAutoService(autoServiceId)
//            val vehicleId = autoService?.vehicleId
//            val locationId = autoService?.locationId
//            if (autoService == null || vehicleId == null || locationId == null) {
//                return null
//            }
//            val mechanic = mechanicRepo.getMechanic(autoService.mechanicId)
//            val vehicle = vehicleRepo.getVehicle(vehicleId)
//            val location = locationRepo.getLocation(locationId)
//            val mechanicUser = userRepo.getUser(mechanic?.userId ?: "")
//
//            if (mechanic == null || vehicle == null || location == null || mechanicUser == null) {
//                return null
//            }
//            return AutoServiceListElements(autoService, mechanic, vehicle, location, mechanicUser)
//        } catch (e: Exception) {
//            print(e)
//            return null
//        }
//    }

    fun updateNotes(notes: String, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val id = autoServiceId
        if (id == null) {
            return
        }
        autoServiceRepo.updateNotes(id, notes, getApplication(), completion)
//        autoServiceRepo.updateNotes(id, notes, getApplication()) { error, autoServiceId
//            Log.w("car swaddle android", "updated notes")
//            completion(error, autoServiceId)
//        }
    }

}

