package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.CouponError
import com.carswaddle.carswaddleandroid.services.CouponErrorType
import com.carswaddle.carswaddleandroid.services.LocationJSON
import com.carswaddle.carswaddleandroid.services.serviceModels.CreateAutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.Price
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.launch
import java.lang.Exception
import java.security.PrivateKey


class SelectDetailsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val vehicleRepo: VehicleRepository
    private val autoServiceRepo: AutoServiceRepository

    init {
        val db = AppDatabase.getDatabase(application)
        vehicleRepo = VehicleRepository(db.vehicleDao())
        autoServiceRepo = AutoServiceRepository(db.autoServiceDao())

        loadVehicles()
    }
    
    val price: LiveData<Price>
        get() = _price
    
    private val _price = MutableLiveData<Price>()

    val vehicles: LiveData<List<Vehicle>>
        get() = _vehicles
    
    val couponError: LiveData<CouponErrorType?>
        get() = _couponError

    private val _couponError = MutableLiveData<CouponErrorType?>()
    
    private val _vehicles = MutableLiveData<List<Vehicle>>()
    
    fun loadVehicles() {
        vehicleRepo.getVehicles(30, 0, getApplication()) { error, vehicleIds ->
            viewModelScope.launch {
                val vs = vehicleRepo.getVehicles(vehicleIds ?: listOf())
                _vehicles.postValue(vs)
            }
        }
    }
    
    fun loadPrice(latitude: Double, longitude: Double, mechanicId: String, oilType: OilType, coupon: String?) {
        val location = LocationJSON(latitude, longitude)
        autoServiceRepo.getPrice(location, mechanicId, oilType, coupon, getApplication()) { error, price ->
            Log.w("price", "Got price back")
            val p = price
            if (p != null) {
                _price.postValue(p)
            }
            if (error != null && error is CouponError) {
                _couponError.postValue(error.couponErrorType)
            } else {
                _couponError.postValue(null)
            }
        }
    }
    
    fun createAndPayForAutoService(createAutoService: CreateAutoService, completion: (error: Throwable?, newAutoService: AutoService?) -> Unit) {
        autoServiceRepo.createAndPayForAutoService(createAutoService, getApplication(), completion)
    }

}

fun CouponErrorType.localizedString(context: Context): String {
    return when(this) {
        CouponErrorType.DEPLETED_REDEMPTIONS -> context.getString(R.string.depleted_redemptions)
        CouponErrorType.EXPIRED -> context.getString(R.string.expired_code)
        CouponErrorType.INCORRECT_CODE -> context.getString(R.string.incorrect_code)
        CouponErrorType.INCORRECT_MECHANIC -> context.getString(R.string.incorrect_mechanic)
        CouponErrorType.OTHER -> context.getString(R.string.other_coupon_error)
    }
}


