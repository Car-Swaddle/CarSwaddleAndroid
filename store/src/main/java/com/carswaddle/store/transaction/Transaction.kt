package com.carswaddle.store.transaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.Payout
import com.carswaddle.carswaddleandroid.services.serviceModels.Transaction as TransactionServiceModel
import com.carswaddle.carswaddleandroid.services.serviceModels.TransactionMetadata
import java.util.*

@Entity
data class Transaction(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "created") val created: Calendar,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "transaction_description") val transactionDescription: String?,
    @ColumnInfo(name = "exchange_rate") val exchangeRate: Int?,
    @ColumnInfo(name = "fee") val fee: Int,
    @ColumnInfo(name = "net") val net: Int,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "mechanic_id") val mechanicId: String?, // Mechanic?
    @ColumnInfo(name = "payout_id") val payoutId: String?,
    @ColumnInfo(name = "transaction_metadata_id") val transactionMetadataId: String?, // TransactionMetadata?
    @ColumnInfo(name = "adjusted_available_on_date") val adjustedAvailableOnDate: Calendar,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "available_on") val availableOn: Calendar
) {
    
    constructor(transaction: TransactionServiceModel): this(
        transaction.id,
        transaction.amount,
        transaction.created(),
        transaction.currency,
        transaction.transactionDescription,
        transaction.exchangeRate,
        transaction.fee,
        transaction.net,
        transaction.source,
        transaction.type,
        transaction.mechanic?.id,
        transaction.payout?.id,
        transaction.transactionMetadata?.identifier,
        transaction.adjustedAvailableOnDate(),
        transaction.status,
        transaction.availableOn()
    )
    
}