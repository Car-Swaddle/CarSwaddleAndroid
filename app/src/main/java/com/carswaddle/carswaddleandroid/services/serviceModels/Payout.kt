package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Payout (
    val identifier: String,
    val amount: Int,
    val arrivalDate: Calendar,
    val created: Calendar,
    val currency: String,
    val payoutDescription: String?,
    val destination: String?,
    // bank_account or card
    val type: String,
    // standard or instant
    val method: String,
    // card, bank_account, or alipay_account
    val sourceType: String,
    val statementDescriptor: String?,
    val failureMessage: String?,
    val failureCode: String?,
    val failureBalanceTransaction: String?,
    val transactions: Set<Transaction>,
    val mechanic: Mechanic?,
    val balanceTransactionID: String?,
    val status: String
)