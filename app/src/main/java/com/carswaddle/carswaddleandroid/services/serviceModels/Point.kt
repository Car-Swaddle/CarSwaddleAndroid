package com.carswaddle.carswaddleandroid.services.serviceModels

data class Point (
    val type: String,
    val coordinates: Array<Float>
) {

    fun latitude(): Float {
        return coordinates[0]
    }

    fun longitude(): Float {
        return coordinates[1]
    }

}
