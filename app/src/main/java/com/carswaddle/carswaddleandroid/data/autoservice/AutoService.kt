package com.carswaddle.carswaddleandroid.data.autoservice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import java.util.*

@Entity
data class AutoService(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "balance_transaction_id") val balanceTransactionId: String?,
    @ColumnInfo(name = "charge_id") val chargeId: String?,
    @ColumnInfo(name = "coupon_id") val couponId: String?,
    @ColumnInfo(name = "creation_date") val creationDate: Calendar?,
    @ColumnInfo(name = "is_canceled") val isCanceled: Boolean?,
    @ColumnInfo(name = "notes") val notes: String?,
    @ColumnInfo(name = "scheduled_date") val scheduledDate: Calendar?,
    @ColumnInfo(name = "status") val status: AutoServiceStatus?,
    @ColumnInfo(name = "transfer_id") val transferId: String?,
    @ColumnInfo(name = "creator_id") val creatorId: String?,
    @ColumnInfo(name = "mechanic_id") val mechanicId: String,
    @ColumnInfo(name = "location_id") val locationId: String,
    @ColumnInfo(name = "vehicle_id") val vehicleId: String?
) {

    constructor(autoService: com.carswaddle.carswaddleandroid.services.serviceModels.AutoService) :
            this(
                autoService.id,
                autoService.balanceTransactionID,
                autoService.chargeID,
                autoService.couponID,
                autoService.createdAt.toCalendar(),
                autoService.isCanceled,
                autoService.notes,
                autoService.scheduledDate?.toCalendar(),
                autoService.status,
                autoService.transferID,
                autoService.userID,
                autoService.mechanicID,
                autoService.location.id,
                autoService.vehicleID
            )
    
    fun startTimeSecondsSinceMidnight(): Int? {
        var c = scheduledDate
        if (c == null) {
            return null
        }
        val date = c.timeInMillis
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        val passed = date - c.timeInMillis
        return (passed / 1000).toInt()
    }

}
