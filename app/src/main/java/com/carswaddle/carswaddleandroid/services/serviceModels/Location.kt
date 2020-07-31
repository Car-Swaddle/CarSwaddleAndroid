package com.carswaddle.carswaddleandroid.services.serviceModels

import android.location.Location
import java.util.*

data class AutoServiceLocation(
    val id: String,
    val point: Point,
    val streetAddress: String,
    val createdAt: Date,
    val updatedAt: Date,
    val autoServiceID: String,
    val mechanicID: String?
)
