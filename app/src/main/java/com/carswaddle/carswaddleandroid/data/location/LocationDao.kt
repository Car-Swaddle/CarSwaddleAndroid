package com.carswaddle.carswaddleandroid.data.location

import androidx.room.Dao
import androidx.room.Query
import com.carswaddle.carswaddleandroid.data.location.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM location WHERE id is (:locationId)")
    fun getLocation(locationId: String): Location

}