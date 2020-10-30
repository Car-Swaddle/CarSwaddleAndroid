package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.app.Application
import android.util.Log
import retrofit2.Call
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan as TemplateTimeSpanModel
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpanRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.generic.DispatchGroup
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceLocation
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.lang.Exception
import java.time.DayOfWeek
import java.util.*


class SelectMechanicViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepo: MechanicRepository
    private val userRepo: UserRepository
    private val timeSpanRepo: TemplateTimeSpanRepository
    private val autoServiceRepo: AutoServiceRepository

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
        autoServiceRepo = AutoServiceRepository(db.autoServiceDao())
    }

    val mechanics: LiveData<List<MechanicListElements>>
        get() = _mechanics

    private val _mechanics = MutableLiveData<List<MechanicListElements>>()

    /// Mechanic: {Weekday: [Slot]}
    val mechanicTimeSlots: LiveData<Map<String,Map<Int, List<TemplateTimeSpan>>>>
        get() = _mechanicTimeSlots

    /// Mechanic: {Weekday: [Slot]}
    private val _mechanicTimeSlots = MutableLiveData<Map<String,Map<Int, List<TemplateTimeSpan>>>>()
    
    /// Map of mechanic ids to list of tempalte time spans
    private var mechanicSlots: MutableMap<String, List<TemplateTimeSpan>> = mutableMapOf()
    
    /// This must be called after the closure for load time slots is called
    fun timeSlots(mechanicId: String, dayOfWeek: Int): List<TemplateTimeSpan> {
        var spans: MutableList<TemplateTimeSpan> = mutableListOf()
        
        val allSpans = mechanicSlots[mechanicId] ?: listOf()
        
        for (s in allSpans) {
            if (s.weekDayInt == dayOfWeek) {
                spans.add(s)
            }
        }
        return spans.sortedBy { it.startTime }
    }
    
    
    private var spanIds: MutableMap<String,List<String>> = mutableMapOf()
    private var autoServiceIds: MutableMap<String, List<String>> = mutableMapOf()
    
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

    }
    
    private var getAutoServicesCall: Call<List<Map<String,Any>>>? = null
    private var getTimeSlotsCall: Call<List<TemplateTimeSpanModel>>? = null
    
    fun loadTimeSlots(mechanicId: String, completion: () -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            val startDate = Calendar.getInstance()
            startDate.add(Calendar.DAY_OF_YEAR, 1)
            startDate.set(Calendar.HOUR_OF_DAY, 0)
            startDate.set(Calendar.MINUTE, 0)
            startDate.set(Calendar.SECOND, 0)
            startDate.set(Calendar.MILLISECOND, 0)
    
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.DAY_OF_YEAR, 8)
            endDate.set(Calendar.HOUR_OF_DAY, 0)
            endDate.set(Calendar.MINUTE, 0)
            endDate.set(Calendar.SECOND, 0)
            endDate.set(Calendar.MILLISECOND, 0)
             
            val filterAutoServiceStatus = listOf<AutoServiceStatus>(
                AutoServiceStatus.scheduled,
                AutoServiceStatus.inProgress,
                AutoServiceStatus.completed
            )
            
            getAutoServicesCall?.cancel()
            getTimeSlotsCall?.cancel()
            
            val group = DispatchGroup()
            
            group.enter()
            getAutoServicesCall = autoServiceRepo.getAutoServicesDate(
                mechanicId,
                startDate,
                endDate,
                filterAutoServiceStatus,
                getApplication()
            ) { error, newAutoServiceIds ->
                Log.w("autoservices date", "autoservice ids $newAutoServiceIds")
                if (newAutoServiceIds != null) {
                    autoServiceIds[mechanicId] = newAutoServiceIds
                }
                group.leave()
            }
            
            group.enter()
            getTimeSlotsCall = mechanicRepo.getTimeSlots(mechanicId, getApplication()) { error, newSpanIds ->
                Log.w("time spans", "got time spans $spanIds")
                if (newSpanIds != null) {
                    spanIds[mechanicId] = newSpanIds
                }
                group.leave()
            }
            
            group.notify {
                viewModelScope.launch(Dispatchers.IO) {
                    val allSpans = timeSpanRepo.getTimeSpans(spanIds[mechanicId] ?: listOf()) ?: listOf()
                    var availableSpans: MutableList<TemplateTimeSpan> = mutableListOf()
                    val allAutoServices: List<AutoService> = autoServiceRepo.getAutoServices(autoServiceIds[mechanicId] ?: listOf())
                    for (span in allSpans) {
                        // loop through all spans for the next 7 days
                        // if an autoservice already exists with the same weekday and start time, remove it
                        
                        for (autoservice in allAutoServices) {
                            val serviceDayOfWeek = autoservice.scheduledDate?.get(Calendar.DAY_OF_WEEK) ?: 0
                            val serviceSecondOfDay = autoservice.startTimeSecondsSinceMidnight() ?: 0
                            if (span.weekDayInt != serviceDayOfWeek && span.startTime != serviceSecondOfDay) {
                                availableSpans.add(span)
                            }
                        }
                    }
                    
                    mechanicSlots[mechanicId] = availableSpans.toList()
                    
                    completion()
                    
//                    var slots = _mechanicTimeSlots.value?.toMutableMap() ?: mutableMapOf()
//                    
//                    var mutableCalSpan: MutableMap<Int, MutableList<TemplateTimeSpan>> = mutableMapOf()
//                    for (span in availableSpans) {
//                        var spans = mutableCalSpan[span.weekDayInt] ?: mutableListOf()
//                        spans.add(span)
//                        mutableCalSpan[span.weekDayInt] = spans
//                    }
//                    
//                    
//                    
//                    val calSpan: Map<Int, List<TemplateTimeSpan>> = Map<Int, List<TemplateTimeSpan>>
//                    
//                    slots[mechanicId] = calSpan
//
//                    _mechanicTimeSlots.postValue(slots)
                    
                }
            }
        }
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