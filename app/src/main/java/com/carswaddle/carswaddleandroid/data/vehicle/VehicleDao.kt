package com.carswaddle.carswaddleandroid.data.vehicle

import androidx.room.Dao
import androidx.room.Query
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicle WHERE id is (:vehicleId)")
    fun getVehicle(vehicleId: String): Vehicle

}