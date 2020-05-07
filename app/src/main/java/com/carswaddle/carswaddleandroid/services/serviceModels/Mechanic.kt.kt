package com.carswaddle.carswaddleandroid.services.serviceModels

data class Mechanic (
    val id: String,
    val isActive: Boolean,
    val dateOfBirth: String,
    val user: User?,
    val userID: String?,

    val profileImageID: String?,
    val address: String,
    val following_url: String,
    val starred_url: String,
    val gists_url: String,
    val type: String,
    val score: Int
)