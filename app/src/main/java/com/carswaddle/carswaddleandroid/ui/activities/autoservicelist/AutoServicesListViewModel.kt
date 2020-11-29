package com.carswaddle.carswaddleandroid.activities.ui.home

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.Extensions.carSwaddlePreferences
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.CalendarConverter
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

private val AUTOSERVICE_IDS_KEY = "AUTOSERVICE_IDS_KEY"

class AutoServicesListViewModel(application: Application) : AndroidViewModel(application) {

    val upcomingAutoServices: LiveData<List<AutoServiceListElements>>
        get() = _upcomingAutoServices

    val pastAutoServices: LiveData<List<AutoServiceListElements>>
        get() = _pastAutoServices
    
    val currentUser: User?
    
    private val autoServiceRepo: AutoServiceRepository
    private val locationRepo: AutoServiceLocationRepository
    private val mechanicRepo: MechanicRepository
    private val userRepo: UserRepository
    private val vehicleRepo: VehicleRepository
    private val serviceEntityRepo: ServiceEntityRepository
    private val oilChangeRepo: OilChangeRepository

    private val _upcomingAutoServices = MutableLiveData<List<AutoServiceListElements>>()

    private val _pastAutoServices = MutableLiveData<List<AutoServiceListElements>>()
    
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

        CoroutineScope(Dispatchers.IO).launch {
            val ids = getAutoServiceIds()
            updateAutoServices(ids)
        }
        
        currentUser = userRepo.getCurrentUser(application)
    }

    private fun loadAutoServices() {
        autoServiceRepo.getAutoServices(
            100,
            0,
            getApplication(),
            listOf<String>(),
            listOf<String>() // listOf("scheduled", "canceled", "inProgress")
        ) { error, autoServiceIds ->
            CoroutineScope(Dispatchers.IO).launch {
                if (autoServiceIds != null) {
                    setAutoServiceIds(autoServiceIds)
                    updateAutoServices(autoServiceIds)
                } else {

                }
            }
        }
    }
    
    suspend private fun updateAutoServices(autoServiceIds: List<String>) {
        var upcomingElements: MutableList<AutoServiceListElements> = ArrayList()
        var pastElements: MutableList<AutoServiceListElements> = ArrayList()
        val now = Calendar.getInstance()
        for (id in autoServiceIds) {
            fetchAutoServiceListElements(id)?.let {
                if (it.autoService.status != AutoServiceStatus.canceled && it.autoService.scheduledDate?.after(now) == true) {
                    upcomingElements.add(it)
                } else {
                    pastElements.add(it)
                }
            }
        }
        _pastAutoServices.postValue(pastElements)
        _upcomingAutoServices.postValue(upcomingElements)
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
    
    private fun preferences(): SharedPreferences {
        val c: Context = getApplication()
        return c.carSwaddlePreferences()
    }

    fun setAutoServiceIds(autoServiceIds: List<String>) {
        val editContext = preferences().edit()
        editContext.putString(AUTOSERVICE_IDS_KEY, autoServiceIds.joinToString(","))
        editContext.apply()
    }

    private fun getAutoServiceIds(): List<String> {
        val ids = preferences().getString(AUTOSERVICE_IDS_KEY, null) ?: return listOf()
        return ids.split(",")
    }
    
}
