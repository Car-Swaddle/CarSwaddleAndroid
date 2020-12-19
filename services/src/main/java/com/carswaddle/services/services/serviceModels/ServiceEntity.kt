package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class ServiceEntity (
    val id: String,
    val entityType: String,
    val createdAt: Date,
    val updatedAt: Date,
    val autoServiceID: String,
    val oilChangeID: String,
    val oilChange: OilChange
)
