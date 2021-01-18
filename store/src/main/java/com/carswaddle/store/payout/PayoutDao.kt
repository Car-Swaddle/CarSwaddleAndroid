package com.carswaddle.store.payout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.store.balance.Balance

@Dao
public abstract class PayoutDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPayout(payout: Payout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPayouts(payout: List<Payout>)

    @Query("SELECT SUM(amount) FROM payout WHERE status is 'inTransit'")
    abstract fun getInTransitPayoutAmount(): Int
    
    @Query("SELECT SUM(amount) FROM payout WHERE status is (:status)")
    abstract fun getAmountSum(status: List<PayoutStatus>): Int
    
}