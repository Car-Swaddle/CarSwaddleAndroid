package com.carswaddle.carswaddleandroid.services.serviceModels

data class Amount(
    val value: Int,
    val currency: String,
    val balanceForAvailable: Balance?,
    val balanceForPending: Balance?,
    val balanceForReserved: Balance?
)
