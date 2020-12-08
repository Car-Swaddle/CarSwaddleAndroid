package com.carswaddle.carswaddleandroid.services.serviceModels

data class Review(
    val id: String,
    val rating: Float,
    val text: String,
    val reviewerID: String,
    val revieweeID: String,
    val autoServiceIDFromUser: String?,
    val autoServiceIDFromMechanic: String?
)
