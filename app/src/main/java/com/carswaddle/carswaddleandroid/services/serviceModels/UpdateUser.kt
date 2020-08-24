package com.carswaddle.carswaddleandroid.services.serviceModels


data class UpdateUser (
    val firstName: String?,
    val lastName: String?,
    val phoeNumber: String?,
    val token: String?,
    val timeZone: String?,
    val adminKey: String?
)