package com.carswaddle.carswaddleandroid.data.autoservice

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carswaddle.carswaddleandroid.data.location.Location
import com.carswaddle.carswaddleandroid.data.location.LocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleRepository


@Dao
public abstract class AutoServiceDao {
//    @Query("SELECT * FROM user")
//    fun getAll(): List<User>
//
//    @Query("SELECT * FROM user WHERE id IN (:userIds)")
//    fun getUsersWithUserIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE id IN (:userId)")
//    fun getUserWithUserId(userId: String): LiveData<User>?
//
//    @Query("SELECT * FROM user WHERE id IN (:userId)")
//    fun getDatUser(userId: String): User?
//

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMechanic(mechanic: Mechanic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertVehicle(vehicle: Vehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAutoService(autoService: AutoService)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocation(location: Location)

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

    @Query("SELECT * FROM location WHERE id IN (:locationId)")
    abstract fun getLocation(locationId: String): Location?

    @Query("SELECT * FROM user WHERE id IN (:userId)")
    abstract fun getUser(userId: String): User?

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertUsers(vararg users: User)
//
//    @Delete
//    fun delete(user: User)
}
