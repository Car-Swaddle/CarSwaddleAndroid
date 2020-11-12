package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.services.LocationJSON
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.Price
import kotlinx.coroutines.launch

class AddVehicleViewModel(application: Application) : AndroidViewModel(application) {

    private val vehicleRepo: VehicleRepository

    init {
        val db = AppDatabase.getDatabase(application)
        vehicleRepo = VehicleRepository(db.vehicleDao())
    }
    
    fun createVehicle(name: String, licensePlate: String, state: String, completion: (success: Boolean) -> Unit) {
        vehicleRepo.createVehicle(name, licensePlate, null, state, getApplication()) { error, vehicleId ->
            completion(error == null && vehicleId != null)
        }
    }

}
