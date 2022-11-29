package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.CouponError
import com.carswaddle.carswaddleandroid.services.CouponErrorType
import com.carswaddle.carswaddleandroid.services.LocationJSON
import com.carswaddle.carswaddleandroid.services.serviceModels.CreateAutoService
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.services.services.serviceModels.CodeCheck
import com.carswaddle.services.services.serviceModels.Price
import com.carswaddle.store.AppDatabase
import kotlinx.coroutines.launch

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

    val codeChecks: LiveData<LinkedHashMap<String, CodeCheck>>
        get() = _codeChecks

    private val _codeChecks = MutableLiveData<LinkedHashMap<String, CodeCheck>>()

    private val codeCheckMap = LinkedHashMap<String, CodeCheck>()
    
    private val _vehicles = MutableLiveData<List<Vehicle>>()
    
    fun loadVehicles() {
        vehicleRepo.getVehicles(30, 0, getApplication()) { error, vehicleIds ->
            viewModelScope.launch {
                val vs = vehicleRepo.getVehicles(vehicleIds ?: listOf())
                _vehicles.postValue(vs)
            }
        }
    }

    fun checkCode(code: String, completion: (error: Throwable?) -> Unit) {
        autoServiceRepo.getCodeCheck(code, getApplication()) { error, codeCheck ->
            if (error != null || codeCheck == null) {
                _couponError.postValue(CouponErrorType.OTHER)
            } else if (codeCheck.error != null) {
                _couponError.postValue(codeCheck.error)
            } else {
                if (codeCheck.coupon != null) {
                    val existingCouponCode = codeCheckMap.values.stream().filter { x -> x.coupon != null }.map { x -> x.coupon?.identifier }.findFirst().orElse(null)
                    if (existingCouponCode != null) {
                        codeCheckMap.remove(existingCouponCode)
                    }
                }
                codeCheckMap[code] = codeCheck
                _couponError.postValue(null)
                _codeChecks.postValue(codeCheckMap)
            }
            completion(error)
        }
    }

    fun removeCode(code: String) {
        codeCheckMap.remove(code)
        _codeChecks.postValue(codeCheckMap)
    }

    fun loadPrice(latitude: Double, longitude: Double, mechanicId: String, oilType: OilType, coupon: String?, giftCardCodes: Collection<String>, completion: (error: Throwable?) -> Unit) {
        val location = LocationJSON(latitude, longitude)
        autoServiceRepo.getPrice(location, mechanicId, oilType, coupon, giftCardCodes, getApplication()) { error, price ->
            Log.w("price", "Got price back")
            val p = price
            if (p != null) {
                _price.postValue(p!!)
            }
            if (error != null && error is CouponError) {
                _couponError.postValue(error.couponErrorType)
            } else {
                _couponError.postValue(null)
            }
            completion(error)
        }
    }
    
    fun createAndPayForAutoService(createAutoService: CreateAutoService, completion: (error: Throwable?) -> Unit) {
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


