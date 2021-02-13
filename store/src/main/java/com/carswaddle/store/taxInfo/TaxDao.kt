package com.carswaddle.store.taxInfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public abstract class TaxDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTaxInfo(taxInfo: TaxInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTaxInfos(taxInfo: List<TaxInfo>)
    
    @Query("SELECT * FROM TaxInfo ORDER BY year DESC")
    abstract fun getTaxInfos(): List<TaxInfo>
    
}