package com.carswaddle.carswaddleandroid.data.vehicleDescription

class VehicleDescriptionRepository(private val vehicleDescriptionDao: VehicleDescriptionDao) {

    suspend fun getVehicleDescription(vehicleDescriptionId: String): VehicleDescription? {
        return vehicleDescriptionDao.getVehicleDescription(vehicleDescriptionId)
    }

}