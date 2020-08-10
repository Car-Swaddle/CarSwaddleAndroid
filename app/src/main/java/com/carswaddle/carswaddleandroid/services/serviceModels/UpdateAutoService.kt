package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class UpdateAutoService (
val status: String?,
val notes: String?,
val vehicleID: String?,
val mechanicID: String?,
val locationID: String?,
val scheduledDate: Date?,
val location: UpdateLocation?
)


data class UpdateLocation(
    val longitude: Double,
    val latitude: Double
)
