package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.user.User

@Dao
interface MechanicDao {

    @Query("SELECT * FROM mechanic WHERE id is (:mechanicId)")
    fun getMechanic(mechanicId: String): Mechanic?

    @Query("SELECT * FROM mechanic WHERE id in (:mechanicIds)")
    fun getMechanics(mechanicIds: List<String>): List<Mechanic>?

    @Query("SELECT * FROM user WHERE id IN (:userId)")
    fun getUser(userId: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMechanic(mechanic: Mechanic)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTimeSpan(user: TemplateTimeSpan)

}
