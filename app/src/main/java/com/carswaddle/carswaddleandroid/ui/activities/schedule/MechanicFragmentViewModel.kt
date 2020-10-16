package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpanRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceLocation
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class SelectMechanicViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepo: MechanicRepository
    private val userRepo: UserRepository
    private val timeSpanRepo: TemplateTimeSpanRepository

    var point: Point? = null
        set(newValue) {
            field = newValue
            loadNearestMechanics()
        }


    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepo = MechanicRepository(db.mechanicDao())
        userRepo = UserRepository(db.userDao())
        timeSpanRepo = TemplateTimeSpanRepository(db.templateTimeSpanDao())
    }

    val mechanics: LiveData<List<MechanicListElements>>
        get() = _mechanics

    private val _mechanics = MutableLiveData<List<MechanicListElements>>()

    fun loadNearestMechanics() {

        val p = point
        if (p == null) {
            return
        }

        mechanicRepo.getNearestMechanics(p.latitude(), p.longitude(), 1000000.0, 100, getApplication()) { error, mechanicIDs ->
            Log.w("mechs", "Got mechanics")
            if (mechanicIDs == null) {
                return@getNearestMechanics
            }
            CoroutineScope(Dispatchers.IO).launch {
                var fetchedMechanics = arrayListOf<MechanicListElements>()
                for (id in mechanicIDs.iterator()) {
                    val mechanic = mechanicRepo.getMechanic(id)
                    if (mechanic == null) {
                        continue
                    }
                    val userID = mechanic.userId
                    if (userID == null) {
                        continue
                    }
                    val user = userRepo.getUser(userID)

                    val timeSpanIDs = mechanic?.scheduleTimeSpanIds ?: arrayListOf()

                    val timeSpans = timeSpanRepo.getTimeSpans(timeSpanIDs) ?: arrayListOf()

                    if (user == null) {
                        continue
                    }

                    val m = MechanicListElements(mechanic, user, timeSpans)
                    fetchedMechanics.add(m)
                    
                    loadStats(mechanic.id)
                }

                _mechanics.postValue(fetchedMechanics)
            }

        }

//        autoServiceRepo.getAutoServices(
//            100,
//            0,
//            getApplication(),
//            listOf<String>(),
//            listOf("scheduled", "canceled", "inProgress")
//        ) { error, autoServiceIds ->
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

    fun loadStats(mechanicId: String) {
        mechanicRepo.getStats(mechanicId, getApplication()) { error, updatedMechanicId ->
            // Get the right mechanic and create a new mutablelive data object with the new changes inserted in for the right mechanic
            var mechanicElements: MutableList<MechanicListElements>? = mechanics.value?.toMutableList()

            if (mechanicElements == null) {
                return@getStats
            }

            val index = mechanicElements.indexOfFirst { it.mechanic.id == mechanicId }

            if (index == null) {
                return@getStats
            }

            val mechanicElement = mechanicElements[index]
            val mechanic = mechanicRepo.getMechanic(mechanicId)
            if (mechanic == null) {
                return@getStats
            }
            val newElement = MechanicListElements(mechanic, mechanicElement.user, mechanicElement.timeSpans)
            mechanicElements[index] = newElement

            _mechanics.postValue(mechanicElements)
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
//            val serviceEntities = serviceEntityRepo.getServiceEntities(autoServiceId)
//
//            if (mechanic == null || vehicle == null || location == null || mechanicUser == null) {
//                return null
//            }
//
//            return AutoServiceListElements(
//                autoService,
//                mechanic,
//                vehicle,
//                location,
//                mechanicUser,
//                serviceEntities
//            )
//        } catch (e: Exception) {
//            print(e)
//            return null
//        }
//    }

}