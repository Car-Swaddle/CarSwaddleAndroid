package com.carswaddle.carswaddleandroid.services.serviceModels

import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.google.android.gms.maps.model.LatLng

class Point {

    val type: String
    val coordinates: Array<Double>

    constructor(type: String, coordinates: Array<Double>) {
        this.type = type
        this.coordinates = coordinates
    }

    constructor(latitude: Double, longitude: Double) {
        type = ""
        coordinates = arrayOf<Double>(longitude, latitude)
    }

    constructor(latLng: LatLng) {
        type = ""
        coordinates = arrayOf<Double>(latLng.longitude, latLng.latitude)
    }

    fun latitude(): Double {
        return coordinates[1]
    }

    fun longitude(): Double {
        return coordinates[0]
    }

}
