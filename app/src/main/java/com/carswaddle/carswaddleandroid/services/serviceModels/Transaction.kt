package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Transaction (
    val identifier: String,
    val amount: Int,
    val created: Calendar,
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
    val adjustedAvailableOnDate: Calendar,
    val status: String,
    val availableOn: Calendar
)
