package com.carswaddle.store.region

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carswaddle.store.taxInfo.TaxInfo

@Dao
public abstract class RegionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRegion(region: Region)

    @Query("SELECT * FROM Region where id is (:id)")
    abstract fun getRegion(id: String): Region

}