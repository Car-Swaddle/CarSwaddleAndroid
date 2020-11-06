package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.Application
import androidx.lifecycle.*
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeRepository
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityRepository
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import kotlinx.coroutines.launch
import java.lang.Exception


class SelectDetailsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val vehicleRepo: VehicleRepository

    init {
        val db = AppDatabase.getDatabase(application)
        vehicleRepo = VehicleRepository(db.vehicleDao())

        loadVehicles()
    }

    val vehicles: LiveData<List<Vehicle>>
        get() = _vehicles

    private val _vehicles = MutableLiveData<List<Vehicle>>()

    private fun loadVehicles() {
        vehicleRepo.getVehicles(10, 0, getApplication()) { error, vehicleIds ->
            viewModelScope.launch {
                val vs = vehicleRepo.getVehicles(vehicleIds ?: listOf())
                _vehicles.postValue(vs)
            }
        }
    }

}

