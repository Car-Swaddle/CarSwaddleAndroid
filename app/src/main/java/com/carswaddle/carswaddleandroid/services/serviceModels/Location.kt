package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Location(
    val id: String,
    val point: Point,
    val streetAddress: String,
    val createdAt: Date,
    val updatedAt: Date,
    val autoServiceID: String,
    val mechanicID: String?
)
