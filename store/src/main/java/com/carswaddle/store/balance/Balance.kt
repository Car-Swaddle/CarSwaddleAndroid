package com.carswaddle.store.balance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Balance(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "available_value") val availableValue: Int,
    @ColumnInfo(name = "available_currency") val availableCurrency: String,
    @ColumnInfo(name = "pending_value") val pendingValue: Int,
    @ColumnInfo(name = "pending_currency") val pendingCurrency: String,
    @ColumnInfo(name = "reserved_value") val reservedValue: Int?,
    @ColumnInfo(name = "reserved_currency") val reservedCurrency: String?,
    @ColumnInfo(name = "mechanic_id") val mechanicId: String
) {

    constructor(balance: com.carswaddle.carswaddleandroid.services.serviceModels.Balance, mechanicId: String) :
            this(
                "balance:" + mechanicId,
                balance.available.first().amount,
                balance.available.first().currency,
                balance.pending.first().amount,
                balance.pending.first().currency,
                balance.connect_reserved?.first()?.amount,
                balance.connect_reserved?.first()?.currency,
                mechanicId
            )

}