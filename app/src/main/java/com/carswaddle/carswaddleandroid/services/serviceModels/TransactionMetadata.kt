package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class TransactionMetadata (
    val identifier: String,
    val stripeTransactionID: String,
    val mechanicCost: Int,
    val drivingDistance: Int,
    val autoServiceID: String,
    val mechanicID: String,
    val createdAt: Calendar,
    val transaction: Transaction?,
    val autoService: AutoService?,
    val receipts: List<TransactionReceipt>
)