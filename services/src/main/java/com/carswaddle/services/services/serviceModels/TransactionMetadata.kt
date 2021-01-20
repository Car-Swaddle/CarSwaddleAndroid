package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class TransactionMetadata (
    val id: String,
    val stripeTransactionID: String,
    val mechanicCost: Int,
    val drivingDistance: Int,
    val autoServiceID: String,
    val mechanicID: String,
    val createdAt: Date,
    val transactionReceipts: List<TransactionReceipt>
)