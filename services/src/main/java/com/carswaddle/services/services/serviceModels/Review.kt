package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Review(
    val id: String,
    val rating: Float,
    val text: String,
    val reviewerID: String,
    val revieweeID: String,
    val autoServiceIDFromUser: String?,
    val autoServiceIDFromMechanic: String?,
    val createdAt: Date,
    val userID: String?,
    val mechanicID: String?,
)
