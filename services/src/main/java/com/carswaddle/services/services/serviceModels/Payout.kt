package com.carswaddle.carswaddleandroid.services.serviceModels

import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.foundation.Extensions.epochToDate
import com.google.gson.annotations.SerializedName
import java.util.*

data class Payout (
    val id: String,
    val amount: Int,
    @SerializedName("arrival_date") val arrivalDouble: Double,
    @SerializedName("created") val createdDouble: Double,
    val currency: String,
    @SerializedName("description") val payoutDescription: String?,
    val destination: String?,
    // bank_account or card
    val type: String,
    // standard or instant
    val method: String,
    // card, bank_account, or alipay_account
    @SerializedName("source_type") val sourceType: String,
    @SerializedName("statement_descriptor") val statementDescriptor: String?,
    @SerializedName("failure_message") val failureMessage: String?,
    @SerializedName("failure_code") val failureCode: String?,
    @SerializedName("failure_balance_transaction") val failureBalanceTransaction: String?,
    val transactions: Set<Transaction>,
    @SerializedName("balance_transaction_id") val balanceTransactionID: String?,
    val status: PayoutStatus
) {

    fun arrivalDate(): Calendar = arrivalDouble.epochToDate().toCalendar()
    fun created(): Calendar = createdDouble.epochToDate().toCalendar()
    
}

enum class PayoutStatus {
    @SerializedName("in_transit") inTransit,
    @SerializedName("paid") paid,
    @SerializedName("pending") pending,
    @SerializedName("canceled") canceled,
    @SerializedName("failed") failed;
    
    fun localizedString(): String {
        return when(this) {
            inTransit -> "In transit"
            paid -> "Paid"
            pending -> "Pending"
            canceled -> "Canceled"
            failed -> "Failed"
        }
    }
    
}