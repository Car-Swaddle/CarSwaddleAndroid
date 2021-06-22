package com.carswaddle.carswaddleandroid.services.serviceModels

data class OilChangePricing (
    val id: String,
    val conventional: Int,
    val blend: Int,
    val synthetic: Int,
    val highMileage: Int,
    val centsPerMile: Int,
    val mechanicID: String
)

data class UpdateOilChangePricing (
    val conventional: Int,
    val blend: Int,
    val synthetic: Int,
    val highMileage: Int,
)