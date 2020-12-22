package com.carswaddle.carswaddleandroid.data.location

import androidx.room.Dao
import androidx.room.Query
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation

@Dao
interface AutoServiceLocationDao {

    @Query("SELECT * FROM autoservicelocation WHERE id is (:locationId)")
    fun getLocation(locationId: String): AutoServiceLocation

}