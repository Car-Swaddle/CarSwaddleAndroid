package com.carswaddle.carswaddlemechanic.ui.calendar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.exp


val dayAutoServiceListCacheValidator: CacheValidator = CacheValidator(90)

class DayAutoServiceListViewModel(application: Application) : AndroidViewModel(application) {

    val dayAutoServices: LiveData<List<AutoServiceListElements>>
        get() = _dayAutoServices
    
    var date: Calendar = Calendar.getInstance()
    set(newValue) {
        field = newValue
        loadAutoServices()
    }

    private val _dayAutoServices = MutableLiveData<List<AutoServiceListElements>>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _showEmptyState = MutableLiveData<Boolean>().apply {
        value = false
    }
    val showEmptyState: LiveData<Boolean> = _showEmptyState

    private val autoServiceRepo: AutoServiceRepository
    private val locationRepo: AutoServiceLocationRepository
    private val mechanicRepo: MechanicRepository
    private val userRepo: UserRepository
    private val vehicleRepo: VehicleRepository
    private val serviceEntityRepo: ServiceEntityRepository
    private val oilChangeRepo: OilChangeRepository
    
    private lateinit var autoServiceListChangeObserver: Observer<List<AutoServiceListElements>>

    init {
        val db = AppDatabase.getDatabase(application)
        autoServiceRepo = AutoServiceRepository(db.autoServiceDao())
        locationRepo = AutoServiceLocationRepository(db.locationDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
        userRepo = UserRepository(db.userDao())
        vehicleRepo = VehicleRepository(db.vehicleDao())
        serviceEntityRepo = ServiceEntityRepository(db.serviceEntityDao())
        oilChangeRepo = OilChangeRepository(db.oilChangeDao())


        autoServiceListChangeObserver = object: Observer<List<AutoServiceListElements>> {
            override fun onChanged(list: List<AutoServiceListElements>) {
                _showEmptyState.postValue(list.count() == 0)
            }
        }
                
        _dayAutoServices.observeForever(autoServiceListChangeObserver)

//        loadAutoServices()

//        CoroutineScope(Dispatchers.IO).launch {
//            val ids = getAutoServiceIds()
//            updateAutoServices(ids)
//        }
                                                                    
//        currentUser = userRepo.getCurrentUser(application)
    }

    override fun onCleared() {
        _dayAutoServices.removeObserver(autoServiceListChangeObserver)
        super.onCleared()
    }

    fun loadAutoServices() {
        val mechanicId = mechanicRepo.getCurrentMechanicId(getApplication())
        
        if (mechanicId == null) {
            return
        }

        val startDate = (date.clone() as Calendar)
        startDate.add(Calendar.DATE, 1)
        startDate.set(Calendar.HOUR_OF_DAY, 0)
        startDate.set(Calendar.MINUTE, 0)
        startDate.set(Calendar.SECOND, 0)
        startDate.set(Calendar.MILLISECOND, 0)
        
        val endDate = startDate.clone() as Calendar
        endDate.add(Calendar.DATE, 1)

        val filterAutoServiceStatus = listOf<AutoServiceStatus>(
            AutoServiceStatus.scheduled,
            AutoServiceStatus.completed,
            AutoServiceStatus.inProgress,
            AutoServiceStatus.canceled
        )
        
        startDate.hashCode()
        val hash = Objects.hashCode(listOf(startDate, endDate, mechanicId))

        val isValid = dayAutoServiceListCacheValidator.isValid(hash)
        if (isValid) {
            CoroutineScope(Dispatchers.IO).async {
                val list: MutableList<AutoServiceListElements> = mutableListOf()
                autoServiceRepo.getAutoService(startDate, endDate, mechanicId).forEach {
                    fetchAutoServiceListElements(it.id)?.let { list.add(it) }
                }
                _dayAutoServices.postValue(list)
            }
        } else {
            autoServiceRepo.getAutoServicesDate(
                mechanicId,
                startDate,
                endDate,
                filterAutoServiceStatus,
                getApplication()
            ) { error, newAutoServiceIds ->
                Log.w("autoservices date", "autoservice ids $newAutoServiceIds")
                CoroutineScope(Dispatchers.IO).async {
                    val list: MutableList<AutoServiceListElements> = mutableListOf()
                    val services = autoServiceRepo.getAutoService(startDate, endDate, mechanicId)
                    services.forEach {
                        fetchAutoServiceListElements(it.id)?.let { list.add(it) }
                    }
                    dayAutoServiceListCacheValidator.update(hash)
                    _dayAutoServices.postValue(list)
                }
            }
        }
    }

    suspend private fun updateAutoServices(autoServiceIds: List<String>) {
//        var upcomingElements: MutableList<AutoServiceListElements> = ArrayList()
//        var pastElements: MutableList<AutoServiceListElements> = ArrayList()
//        val now = Calendar.getInstance()
//        for (id in autoServiceIds) {
//            fetchAutoServiceListElements(id)?.let {
//                if (it.autoService.status != AutoServiceStatus.canceled && it.autoService.scheduledDate?.after(now) == true) {
//                    upcomingElements.add(it)
//                } else {
//                    pastElements.add(it)
//                }
//            }
//        }
//        _dayAutoServices.postValue()
//        _pastAutoServices.postValue(pastElements)
//        _upcomingAutoServices.postValue(upcomingElements)
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
            val creator = userRepo.getUser(autoService.creatorId ?: "")

            if (mechanic == null || vehicle == null || location == null || mechanicUser == null) {
                return null
            }

            return AutoServiceListElements(
                autoService,
                mechanic,
                vehicle,
                location,
                mechanicUser,
                serviceEntities,
                null,
                creator
            )
        } catch (e: Exception) {
            print(e)
            return null
        }
    }
    
}


//class Cache<Type>(val expireAfterSeconds: Int) {
//
//    private val dict: MutableMap<Int, Type> = mutableMapOf()
//    
//    private val validator: CacheValidator = CacheValidator(expireAfterSeconds)
//    
//    fun setValue(value: Type, hashCode: Int) {
//        dict[hashCode] = value
//    }
//    
//    fun value(hashCode: Int): Type? {
//        if (validator.isValid(hashCode)) {
//            return dict[hashCode]
//        } else {
//            validator.delete(hashCode)
//            dict.remove(hashCode)
//            return null
//        }
//    }
//    
//}


class CacheValidator(val expireAfterSeconds: Int) {
    
    private val dict: MutableMap<Int, Date> = mutableMapOf()
    
    fun delete(hashCode: Int) {
        dict.remove(hashCode)
    }
    
    fun update(hashCode: Int) {
        dict[hashCode] = Date()
    }
    
    fun isValid(hashCode: Int): Boolean {
        val refreshDate = dict[hashCode]
        if (refreshDate == null) {
            return false
        }
        val diffInMs: Long = refreshDate.time - Date().time
        val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        return diffInSec <= expireAfterSeconds
    }
    
    
}
