package com.carswaddle.store.transaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public abstract class TransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTransactionMetadata(transactionMetadata: TransactionMetadata)
    
    @Query("SELECT * FROM `transaction` WHERE id in (:ids) AND type != 'payout' ORDER BY available_on DESC, created DESC")
    abstract fun getTransactionExcludingPayouts(ids: List<String>): List<Transaction>

    @Query("SELECT * FROM `transaction` WHERE id = (:id)")
    abstract fun getTransaction(id: String): Transaction?
    
    @Query("SELECT * FROM 'TransactionMetadata' WHERE stripe_transaction_id == (:transactionId)")
    abstract fun getTransactionMetadata(transactionId: String): TransactionMetadata?
    
}