package com.carswaddle.carswaddleandroid.services.serviceModels

import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.foundation.Extensions.epochToDate
import com.google.gson.annotations.SerializedName
import java.util.*

data class Transaction (
    val id: String,
    val amount: Int,
    @SerializedName("created") val createdDouble: Double,
    val currency: String,
    @SerializedName("description") val transactionDescription: String?,
    val exchangeRate: Int?,
    val fee: Int,
    val net: Int,
    val source: String,
    val type: TransactionType,
    val payout: Payout?,
    @SerializedName("car_swaddle_meta") val transactionMetadata: TransactionMetadata?,
    val status: String,
    @SerializedName("available_on") val availableOnDouble: Double,
    @SerializedName("reporting_category") val reportingCategory: String?
) {
    
    fun created(): Calendar = createdDouble.epochToDate().toCalendar()
    fun availableOn(): Calendar = availableOnDouble.epochToDate().toCalendar()
    fun adjustedAvailableOnDate(): Calendar {
        val newDate = availableOn().clone() as Calendar
        newDate.set(Calendar.HOUR_OF_DAY, 0)
        newDate.set(Calendar.MINUTE, 0)
        newDate.set(Calendar.SECOND, 0)
        newDate.set(Calendar.MILLISECOND, 0)
        return newDate
    }
    
}



enum class TransactionType {
    @SerializedName("adjustment") adjustment,
    @SerializedName("advance") advance,
    @SerializedName("advance_funding") advanceFunding,
    @SerializedName("application_fee") applicationFee,
    @SerializedName("application_fee_refund") applicationFeeRefund,
    @SerializedName("charge") charge,
    @SerializedName("connect_collection_transfer") connectCollectionTransfer,
    @SerializedName("issuing_authorization_hold") issuingAuthorizationHold,
    @SerializedName("issuing_authorization_release") issuingAuthorizationRelease,
    @SerializedName("issuing_transaction") issuingTransaction,
    @SerializedName("payment") payment,
    @SerializedName("payment_failure_refund") paymentFailureRefund,
    @SerializedName("payment_refund") paymentRefund,
    @SerializedName("payout") payout,
    @SerializedName("payout_cancel") payoutCancel,
    @SerializedName("payout_failure") payoutFailure,
    @SerializedName("refund") refund,
    @SerializedName("refund_failure") refundFailure,
    @SerializedName("reserve_transaction") reserveTransaction,
    @SerializedName("reserved_funds") reservedFunds,
    @SerializedName("stripe_fee") stripeFee,
    @SerializedName("stripe_fx_fee") stripeFxFee,
    @SerializedName("tax_fee") taxFee,
    @SerializedName("topup") topup,
    @SerializedName("topup_reversal") topupReversal,
    @SerializedName("transfer") transfer,
    @SerializedName("transfer_cancel") transferCancel,
    @SerializedName("transfer_failure") transferFailure,
    @SerializedName("transfer_refund") transferRefund,
}
