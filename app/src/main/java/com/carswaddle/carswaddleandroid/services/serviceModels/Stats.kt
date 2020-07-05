package com.carswaddle.carswaddleandroid.services.serviceModels

data class Stats (
     val averageRating: Double,
     val numberOfRatings: Int,
     val autoServicesProvided: Int,
     val mechanic: Mechanic?
)