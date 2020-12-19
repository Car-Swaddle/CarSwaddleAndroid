package com.carswaddle.carswaddleandroid.services.serviceModels

data class Region (
    val identifier: String,
    val longitude: Double,
    val latitude: Double,
    val radius: Double,
    val mechanic: Mechanic?
)