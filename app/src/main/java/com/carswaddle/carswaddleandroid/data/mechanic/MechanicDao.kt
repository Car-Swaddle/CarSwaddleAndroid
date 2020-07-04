package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MechanicDao {

    @Query("SELECT * FROM mechanic WHERE id is (:mechanicId)")
    fun getMechanic(mechanicId: String): Mechanic

}
