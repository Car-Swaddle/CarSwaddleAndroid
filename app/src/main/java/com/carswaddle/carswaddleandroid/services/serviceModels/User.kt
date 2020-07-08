package com.carswaddle.carswaddleandroid.services.serviceModels

data class AuthResponse (
    val token: String?,
    val user: User?,
    val mechanic: Mechanic?
)

data class User (
    val id: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val imageID: String?,
    val email: String,
    val isEmailVerified: Boolean?,
    val isPhoneNumberVerified: Boolean?,
    val timeZone: String,
    val mechanic: Mechanic?,
    val mechanicID: String?
)
