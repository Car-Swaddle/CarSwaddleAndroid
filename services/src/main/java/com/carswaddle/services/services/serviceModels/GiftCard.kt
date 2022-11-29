package com.carswaddle.services.services.serviceModels

import java.time.Instant

data class GiftCard (
    val id: String,
    val code: String,
    val startingBalance: Int,
    val remainingBalance: Int,
    val expiration: Instant,
)
