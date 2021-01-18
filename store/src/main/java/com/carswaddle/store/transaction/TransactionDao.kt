package com.carswaddle.store.transaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public abstract class TransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTransaction(transaction: Transaction)
    
    @Query("SELECT * FROM `transaction` WHERE id in (:ids) ORDER BY available_on DESC")
    abstract fun getTransaction(ids: List<String>): List<Transaction>
    
}