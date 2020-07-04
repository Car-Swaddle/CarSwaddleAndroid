package com.carswaddle.carswaddleandroid.services.serviceModels

data class BankAccount (
    val identifier: String,
    val accountID: String,
    val accountHolderName: String,
    val accountHolderType: String,
    val bankName: String,
    val country: String,
    val currency: String,
    val defaultForCurrency: Boolean,
    val fingerprint: String,
    val last4: String,
    val routingNumber: String,
    val status: String,
    val mechanic: Mechanic?
)