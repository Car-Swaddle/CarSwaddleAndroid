package com.carswaddle.carswaddleandroid.services.serviceModels


data class UpdateUser (
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val token: String?,
    val referrerId: String?,
    val pushTokenType: String?,
    val timeZone: String?,
    val adminKey: String?
)
