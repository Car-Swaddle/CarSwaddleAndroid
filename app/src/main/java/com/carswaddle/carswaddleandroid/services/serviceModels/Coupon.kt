package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Coupon (
    val identifier: String,
    val creationDate: Calendar,
    val createdByUserID: String,
    val createdByMechanicID: String?,
    val discountBookingFee: Boolean,
    val isCorporate: Boolean,
    val name: String,
    val redeemBy: Calendar,
    val redemptions: Int,
    val updatedAt: Calendar,
    val user: User?,
    val autoservices: List<AutoService>
)