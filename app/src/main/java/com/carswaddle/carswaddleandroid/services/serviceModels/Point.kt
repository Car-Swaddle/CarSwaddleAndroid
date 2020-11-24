package com.carswaddle.carswaddleandroid.services.serviceModels

import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.google.android.gms.maps.model.LatLng

class Point {

    val type: String
    val coordinates: Array<Double>
    
    var streetAddress: String? = null

    constructor(type: String, coordinates: Array<Double>, streetAddress: String?) {
        this.type = type
        this.coordinates = coordinates
        this.streetAddress = streetAddress
    }

    constructor(latitude: Double, longitude: Double, streetAddress: String?) {
        type = ""
        coordinates = arrayOf<Double>(longitude, latitude)
        this.streetAddress = streetAddress
    }

    constructor(latLng: LatLng, streetAddress: String?) {
        type = ""
        coordinates = arrayOf<Double>(latLng.longitude, latLng.latitude)
        this.streetAddress = streetAddress
    }

    fun latitude(): Double {
        return coordinates[1]
    }

    fun longitude(): Double {
        return coordinates[0]
    }

}
