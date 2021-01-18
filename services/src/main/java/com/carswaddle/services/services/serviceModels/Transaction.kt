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
    val type: String,
    val payout: Payout?,
    val transactionMetadata: TransactionMetadata?,
    val status: String,
    @SerializedName("available_on") val availableOnDouble: Double,
    @SerializedName("reporting_category") val reportingCategory: String?
) {
    
    fun created(): Calendar = createdDouble.epochToDate().toCalendar()
    fun availableOn(): Calendar = availableOnDouble.epochToDate().toCalendar()
    fun adjustedAvailableOnDate(): Calendar {
        val newDate = availableOn().clone() as Calendar
        newDate.set(Calendar.HOUR_OF_DAY, 0)
        return newDate
    }
    
}
