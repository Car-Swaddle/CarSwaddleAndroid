package com.carswaddle.store.taxInfo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.TaxInfo
import java.util.*

@Entity
data class TaxInfo(
    @PrimaryKey val year: String,
    @ColumnInfo(name = "meters_driven") val metersDriven: Int,
    @ColumnInfo(name = "mechanic_cost_in_cents") val mechanicCostInCents: Int,
) {

    constructor(taxInfo: TaxInfo) :
            this(
                taxInfo.taxYear,
                taxInfo.metersDriven,
                taxInfo.mechanicCostInCents
            )

}