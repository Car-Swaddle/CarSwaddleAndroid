package com.carswaddle.store.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import com.carswaddle.carswaddleandroid.services.serviceModels.TransactionMetadata as TransactionMetadataServiceModel
import java.util.*

@Entity
data class TransactionMetadata(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "stripe_transaction_id") val stripeTransactionID: String,
    @ColumnInfo(name = "mechanic_cost") val mechanicCost: Int,
    @ColumnInfo(name = "driving_distance") val drivingDistance: Int,
    @ColumnInfo(name = "auto_service_id") val autoServiceID: String,
    @ColumnInfo(name = "mechanic_id") val mechanicID: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
) {
    
    constructor(transactionMetadata: TransactionMetadataServiceModel): this(
        transactionMetadata.id,
        transactionMetadata.stripeTransactionID,
        transactionMetadata.mechanicCost,
        transactionMetadata.drivingDistance,
        transactionMetadata.autoServiceID,
        transactionMetadata.mechanicID,
        transactionMetadata.createdAt,
    )
    
}