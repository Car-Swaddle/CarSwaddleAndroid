package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class TransactionReceipt (
    val identifier: String,
    val receiptPhotoID: String,
    val transactionMetadata: TransactionMetadata?,
    val createdAt: Calendar
)