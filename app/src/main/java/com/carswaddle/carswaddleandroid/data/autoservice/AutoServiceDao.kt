package com.carswaddle.carswaddleandroid.data.autoservice

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AutoServiceDao {
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
    fun insertAutoService(autoService: AutoService)

    @Query("SELECT * FROM autoService WHERE creator_id is (:userId) ORDER BY creation_date ASC")
    fun getAutoServicesForUser(userId: String): Array<AutoService>

    @Query("SELECT * FROM autoService WHERE id IN (:autoServiceIds)")
    fun getAutoServicesWithIds(autoServiceIds: Array<String>): LiveData<Array<AutoService>>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertUsers(vararg users: User)
//
//    @Delete
//    fun delete(user: User)
}
