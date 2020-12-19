package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository
import com.carswaddle.store.AppDatabase

class AddVehicleViewModel(application: Application) : AndroidViewModel(application) {

    private val vehicleRepo: VehicleRepository

    init {
        val db = AppDatabase.getDatabase(application)
        vehicleRepo = VehicleRepository(db.vehicleDao())
    }
    
    fun createVehicle(name: String, licensePlate: String, state: String, completion: (vehicleId: String?) -> Unit) {
        vehicleRepo.createVehicle(name, licensePlate, null, state, getApplication()) { error, vehicleId ->
            completion(vehicleId)
        }
    }

}
