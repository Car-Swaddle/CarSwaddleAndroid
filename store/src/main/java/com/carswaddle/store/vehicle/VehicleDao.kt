package com.carswaddle.carswaddleandroid.data.vehicle

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicle WHERE id is (:vehicleId)")
    fun getVehicle(vehicleId: String): Vehicle

    @Query("SELECT * FROM vehicle WHERE id in (:vehicleIds)")
    fun getVehicles(vehicleIds: List<String>): List<Vehicle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehicle(vehicle: Vehicle)

}
