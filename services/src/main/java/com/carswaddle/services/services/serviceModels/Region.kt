package com.carswaddle.carswaddleandroid.services.serviceModels

data class Region (
    val id: String,
    val longitude: Double,
    val latitude: Double,
    val radius: Double,
    val mechanic: Mechanic?
)

data class UpdateRegion(
    val latitude: Double,
    val longitude: Double,
    val radius: Double, // in meters
)
