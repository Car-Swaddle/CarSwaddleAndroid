package com.carswaddle.carswaddleandroid.data.autoservice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class AutoService(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "balance_transaction_id") val balanceTransactionId: String?,
    @ColumnInfo(name = "charge_id") val chargeId: String?,
    @ColumnInfo(name = "coupon_id") val couponId: String?,
    @ColumnInfo(name = "creation_date") val creationDate: Date?,
    @ColumnInfo(name = "is_canceled") val isCanceled: Boolean?,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "scheduled_date") val scheduledDate: Date?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "transfer_id") val transferId: String?,
    @ColumnInfo(name = "creator_id") val creatorId: String?,
    @ColumnInfo(name = "mechanic_id") val mechanicId: String,
    @ColumnInfo(name = "vehicle_id") val vehicleId: String?) {

    constructor(autoService: com.carswaddle.carswaddleandroid.services.serviceModels.AutoService) :
            this(autoService.id, autoService.balanceTransactionID, autoService.chargeID, autoService.couponID, autoService.createdAt, autoService.isCanceled, autoService.notes, autoService.scheduledDate, autoService.status, autoService.transferID, autoService.userID, autoService.mechanicID, autoService.vehicleID)

}
