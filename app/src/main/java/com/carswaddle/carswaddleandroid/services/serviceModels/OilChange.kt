package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class OilChange(
    val id: String,
    val oilType: String,
    val createdAt: Date,
    val updatedAt: Date
)
