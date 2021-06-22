package com.carswaddle.carswaddlemechanic.ui.autoservice_details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.Review.ReviewRepository
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.CreateReview
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.store.AppDatabase
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
    private val reviewRepo: ReviewRepository

    init {
        val db = AppDatabase.getDatabase(application)
        autoServiceRepo = AutoServiceRepository(db.autoServiceDao())
        locationRepo = AutoServiceLocationRepository(db.locationDao())
        mechanicRepo = MechanicRepository(db.mechanicDao())
        userRepo = UserRepository(db.userDao())
        vehicleRepo = VehicleRepository(db.vehicleDao())
        serviceEntityRepo = ServiceEntityRepository(db.serviceEntityDao())
        oilChangeRepo = OilChangeRepository(db.oilChangeDao())
        reviewRepo = ReviewRepository(db.reviewDao())

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
                    _autoServiceElement.postValue(fetchAutoServiceListElements(it))
                }
            }, { error, autoServiceId ->
                if (error == null && autoServiceId != null) {
                    viewModelScope.launch {
                        _autoServiceElement.postValue(fetchAutoServiceListElements(autoServiceId))
                        autoServiceElement.value?.serviceEntities?.let {
                            if (it.size > 0) {
                                val service = it.get(0)
                                val o = oilChangeRepo.getOilChange(service.oilChangeID)
                                if (o != null) {
                                    _oilChange.postValue(o!!)
                                }
                            }
                        }
                    }
                } else {

                }
            })
        }
    }

    fun createReview(createReview: CreateReview, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val id = autoServiceId
        if (id == null) {
            return
        }
        autoServiceRepo.createReview(id, createReview, getApplication(), completion)
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

            var oilChange: OilChange? = null

            val oilChangeId = serviceEntities?.firstOrNull()?.oilChangeID
            if (oilChangeId != null) {
                oilChange = oilChangeRepo.getOilChange(oilChangeId)
            }

            val review = reviewRepo.getReview(autoService.reviewFromUserId ?: "")

            if (mechanic == null || vehicle == null || location == null || mechanicUser == null) {
                return null
            }
            return AutoServiceListElements(autoService, mechanic, vehicle, location, mechanicUser, serviceEntities, review, creator, oilChange)
        } catch (e: Exception) {
            print(e)
            return null
        }
    }

    fun cancelAutoService(context: Context, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val id = autoServiceId
        if (id == null) {
            return completion(AutoServiceIdNotSet(), null)
        }
        autoServiceRepo.cancelAutoService(id, context) { error, autoServiceId ->
            viewModelScope.launch {
                val id = autoServiceId
                if (id != null) {
                    _autoServiceElement.postValue(fetchAutoServiceListElements(id))
                }
            }
        }
    }
    
    fun setAutoServiceStatus(autoServiceStatus: AutoServiceStatus, context: Context, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val id = autoServiceId
        if (id == null) {
            return completion(AutoServiceIdNotSet(), null)
        }
        autoServiceRepo.setAutoServiceStatus(autoServiceStatus, id, context) { error, id ->
            viewModelScope.launch {
                if (id != null) {
                    _autoServiceElement.postValue(fetchAutoServiceListElements(id))
                }
                completion(error, id)
            }
        }
    }

    fun updateNotes(notes: String, context: Context, completion: (error: Throwable?, autoServiceId: String?) -> Unit) {
        val id = autoServiceId
        if (id == null) {
            return completion(AutoServiceIdNotSet(), null)
        }
        autoServiceRepo.updateNotes(id, notes, context, completion)
    }

}

class AutoServiceIdNotSet() : Throwable() {}
