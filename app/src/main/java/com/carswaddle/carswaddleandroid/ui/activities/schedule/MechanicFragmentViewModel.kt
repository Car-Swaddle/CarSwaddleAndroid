package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.app.Application
import android.util.Log
import retrofit2.Call
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan as TemplateTimeSpanModel
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpanRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.generic.DispatchGroup
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.Point
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    
    /// Map of mechanic ids to list of template time spans. The list of time spans is all time slots the mechanic originally set, including those that
    /// are potentially filled by existing appointments. You must remove all time slots that shouldn't be displayed.
    private var mechanicSlots: MutableMap<String, List<TemplateTimeSpan>> = mutableMapOf()
    
    /// This must be called after the closure for load time slots is called.
    /// calendar - The day whose available time slots will be returned. Must have hours, minutes, seconds and milliseconds zero'd out.
    fun timeSlots(mechanicId: String, calendar: Calendar): List<TemplateTimeSpan> {
        var spans: MutableList<TemplateTimeSpan> = mutableListOf()
        
        val allSpans = mechanicSlots[mechanicId] ?: listOf()
        // The day of the week of the calendar parameter. Must subtract 1 because Car Swaddle server is 0 based. Calendar is 1 based.
        val calendarDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        
        var autoServicesOnDay: MutableList<AutoService> = mutableListOf()
        for (a in autoServices) {
            
            var zerodScheduledDate = a.scheduledDate?.clone() as? Calendar
            if (zerodScheduledDate == null) {
                continue
            }
            
            zerodScheduledDate.set(Calendar.HOUR_OF_DAY, 0)
            zerodScheduledDate.set(Calendar.MINUTE, 0)
            zerodScheduledDate.set(Calendar.SECOND, 0)
            zerodScheduledDate.set(Calendar.MILLISECOND, 0)
            
            if (zerodScheduledDate == calendar) {
                autoServicesOnDay.add(a)
            }
        }
        
        for (s in allSpans) {
            var slotAvailable = true
            for (a in autoServicesOnDay) {
                var aStartTime = a.startTimeSecondsSinceMidnight()
                if (aStartTime == null) {
                    continue
                }
                if (s.startTime == aStartTime) {
                    slotAvailable = false
                }
            }
            
            if (s.weekDayInt == calendarDayOfWeek && slotAvailable) {
                spans.add(s)
            }
        }
        return spans.sortedBy { it.startTime }
    }
    
    
    private var spanIds: MutableMap<String,List<String>> = mutableMapOf()
    private var autoServiceIds: MutableMap<String, List<String>> = mutableMapOf()
    
    private var autoServices: List<AutoService> = listOf()
    
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
    
    private var getAutoServicesCall: Call<List<com.carswaddle.carswaddleandroid.services.serviceModels.AutoService>>? = null
    private var getTimeSlotsCall: Call<List<TemplateTimeSpanModel>>? = null
    
    fun loadTimeSlots(mechanicId: String, completion: (error: Throwable?) -> Unit) {

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
            
            var groupError: Throwable? = null
            
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
                groupError = error
                group.leave()
            }
            
            group.enter()
            getTimeSlotsCall = mechanicRepo.getTimeSlots(mechanicId, getApplication()) { error, newSpanIds ->
                Log.w("time spans", "got time spans $spanIds")
                if (newSpanIds != null) {
                    spanIds[mechanicId] = newSpanIds
                }
                groupError = error
                group.leave()
            }
            
            group.notify {
                viewModelScope.launch(Dispatchers.IO) {
                    val allSpans = timeSpanRepo.getTimeSpans(spanIds[mechanicId] ?: listOf()) ?: listOf()
                    autoServices = autoServiceRepo.getAutoServices(autoServiceIds[mechanicId] ?: listOf())
                    mechanicSlots[mechanicId] = allSpans.toList()
                    completion(groupError)
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

            if (mechanicElements == null) {
                return@getStats
            }
            _mechanics.postValue(mechanicElements!!)
        }
    }

}