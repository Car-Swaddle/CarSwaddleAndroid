package com.carswaddle.carswaddleandroid.data.user

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun getUsersWithUserIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userId)")
    fun getUserWithUserId(userId: String): LiveData<User>?

    @Query("SELECT * FROM user WHERE id IN (:userId)")
    fun getDatUser(userId: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg users: User)

    @Delete
    fun delete(user: User)
}
