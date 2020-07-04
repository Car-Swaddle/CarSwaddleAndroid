package com.carswaddle.carswaddleandroid.data.vehicleDescription

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VehicleDescriptionDao {

    @Query("SELECT * FROM vehicleDescription WHERE id is (:vehicleDescriptionId)")
    fun getVehicleDescription(vehicleDescriptionId: String): VehicleDescription

}