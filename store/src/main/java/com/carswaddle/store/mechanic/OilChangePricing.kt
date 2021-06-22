package com.carswaddle.store.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.OilChangePricing
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateOilChangePricing

@Entity
data class OilChangePricing(
    @PrimaryKey val id: String,
    @ColumnInfo val conventional: Int,
    @ColumnInfo val blend: Int,
    @ColumnInfo val synthetic: Int,
    @ColumnInfo val highMileage: Int,
    @ColumnInfo val centsPerMile: Int,
    @ColumnInfo val mechanicID: String) {

    constructor(oilChangePricing: OilChangePricing) :
            this(oilChangePricing.id,
            oilChangePricing.conventional,
            oilChangePricing.blend,
            oilChangePricing.synthetic,
            oilChangePricing.highMileage,
            oilChangePricing.centsPerMile,
            oilChangePricing.mechanicID)

    
    fun updatePricing(): UpdateOilChangePricing {
        return UpdateOilChangePricing(
            this.conventional,
            this.blend,
            this.synthetic,
            this.highMileage,
        )
    }
    
}
