package com.carswaddle.store.balance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
public abstract class BalanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertBalance(balance: Balance)

    @Query("SELECT * FROM balance WHERE mechanic_id is (:mechanicId)")
    abstract fun getBalance(mechanicId: String): Balance?
    
}