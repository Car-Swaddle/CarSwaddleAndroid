package com.carswaddle.store.payout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.Transaction
import java.util.*

@Entity
data class Payout(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "arrival_date") val arrivalDate: Calendar,
    @ColumnInfo(name = "created") val created: Calendar,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "payout_description") val payoutDescription: String?,
    @ColumnInfo(name = "destination") val destination: String?,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "method") val method: String,
    @ColumnInfo(name = "source_type") val sourceType: String,
    @ColumnInfo(name = "statement_descriptor") val statementDescriptor: String?,
    @ColumnInfo(name = "failure_message") val failureMessage: String?,
    @ColumnInfo(name = "failure_code") val failureCode: String?,
    @ColumnInfo(name = "failure_balance_transaction") val failureBalanceTransaction: String?,
//    @ColumnInfo(name = "transactions") val transactions: Set<Transaction>,
    @ColumnInfo(name = "balance_transaction_id") val balanceTransactionID: String?,
    @ColumnInfo(name = "status") val status: PayoutStatus
) {

    constructor(payout: com.carswaddle.carswaddleandroid.services.serviceModels.Payout) :
            this(
                payout.id,
                payout.amount,
                payout.arrivalDate(),
                payout.created(),
                payout.currency,
                payout.payoutDescription,
                payout.destination,
                payout.type,
                payout.method,
                payout.sourceType,
                payout.statementDescriptor,
                payout.failureMessage,
                payout.failureCode,
                payout.failureBalanceTransaction,
                payout.balanceTransactionID,
                payout.status
            )

}