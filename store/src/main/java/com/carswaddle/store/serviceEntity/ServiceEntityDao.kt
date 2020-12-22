package com.carswaddle.carswaddleandroid.data.serviceEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public abstract class ServiceEntityDao {

    @Query("SELECT * FROM serviceentity WHERE id is (:serviceEntityID)")
    abstract fun getServiceEntity(serviceEntityID: String): ServiceEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertServiceEntity(serviceEntity: ServiceEntity)

    @Query("SELECT * FROM serviceentity WHERE auto_service_id is (:autoServiceID)")
    abstract fun getServiceEntities(autoServiceID: String): List<ServiceEntity>

}
