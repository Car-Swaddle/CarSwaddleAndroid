package com.carswaddle.carswaddleandroid.data.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun getUsersWithUserIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM mechanic WHERE id IS (:mechanicId)")
    fun getMechanic(mechanicId: String): Mechanic?

    @Query("SELECT * FROM user WHERE id = (:userId)")
    fun getUserWithUserId(userId: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMechanic(mechanic: Mechanic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)

    @Delete
    fun delete(user: User)
}
