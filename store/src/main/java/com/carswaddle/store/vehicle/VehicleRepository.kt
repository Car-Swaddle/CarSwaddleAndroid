package com.carswaddle.carswaddleandroid.data.vehicle

class VehicleRepository(private val vehicleDao: VehicleDao) {

    suspend fun getVehicle(vehicleId: String): Vehicle? {
        return vehicleDao.getVehicle(vehicleId)
    }

}