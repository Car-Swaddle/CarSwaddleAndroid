package com.carswaddle.carswaddleandroid.data.oilChange

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange


@Dao
public abstract class OilChangeDao {

    @Query("SELECT * FROM oilchange WHERE id is (:oilChangeID)")
    abstract fun getOilChange(oilChangeID: String): OilChange?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOilChange(oilChange: OilChange)

}
