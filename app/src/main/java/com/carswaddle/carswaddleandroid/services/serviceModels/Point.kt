package com.carswaddle.carswaddleandroid.services.serviceModels

data class Point (
    val type: String,
    val coordinates: Array<Double>
) {

    fun latitude(): Double {
        return coordinates[0]
    }

    fun longitude(): Double {
        return coordinates[1]
    }

}
