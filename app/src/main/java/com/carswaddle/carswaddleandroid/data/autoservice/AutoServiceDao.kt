package com.carswaddle.carswaddleandroid.data.autoservice

import androidx.room.*
import com.carswaddle.carswaddleandroid.data.Review.Review
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntity

@Dao
public abstract class AutoServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMechanic(mechanic: Mechanic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOilChange(oilChange: OilChange)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertServiceEntity(serviceEntity: ServiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertVehicle(vehicle: Vehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAutoService(autoService: AutoService)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocation(location: AutoServiceLocation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertReview(review: Review)

    @Query("SELECT * FROM autoService WHERE creator_id is (:userId) ORDER BY creation_date ASC")
    abstract fun getAutoServicesForUser(userId: String): List<AutoService>

    @Query("SELECT * FROM autoService WHERE id IN (:autoServiceIds)")
    abstract fun getAutoServicesWithIds(autoServiceIds: List<String>): List<AutoService>

    @Query("SELECT * FROM autoService WHERE id IN (:autoServiceId)")
    abstract fun getAutoService(autoServiceId: String): AutoService?

    @Query("SELECT * FROM mechanic WHERE id IN (:mechanicId)")
    abstract fun getMechanic(mechanicId: String): Mechanic?

    @Query("SELECT * FROM vehicle WHERE id IN (:vehicleId)")
    abstract fun getVehicle(vehicleId: String): Vehicle

    @Query("SELECT * FROM autoservicelocation WHERE id IN (:locationId)")
    abstract fun getLocation(locationId: String): AutoServiceLocation?

    @Query("SELECT * FROM review WHERE id IN (:reviewId)")
    abstract fun getReview(reviewId: String): Review?
    
    @Query("SELECT * FROM user WHERE id IN (:userId)")
    abstract fun getUser(userId: String): User?

}
