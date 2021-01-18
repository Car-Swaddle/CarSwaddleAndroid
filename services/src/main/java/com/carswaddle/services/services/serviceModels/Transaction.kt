package com.carswaddle.carswaddleandroid.services.serviceModels

import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.google.gson.annotations.SerializedName
import java.util.*

data class Transaction (
    val id: String,
    val amount: Int,
    @SerializedName("create") val createdDouble: Double,
    val currency: String,
    val transactionDescription: String?,
    val exchangeRate: Int?,
    val fee: Int,
    val net: Int,
    val source: String,
    val type: String,
    val mechanic: Mechanic?,
    val payout: Payout?,
    val transactionMetadata: TransactionMetadata?,
    @SerializedName("adjustedAvailableOnDate") val adjustedAvailableOnDouble: Double,
    val status: String,
    @SerializedName("availableOn") val availableOnDouble: Double
) {
    
    fun created(): Calendar = Date(createdDouble.toLong()).toCalendar()
    fun adjustedAvailableOnDate(): Calendar = Date(adjustedAvailableOnDouble.toLong()).toCalendar()
    fun availableOn(): Calendar = Date(availableOnDouble.toLong()).toCalendar()
    
}
