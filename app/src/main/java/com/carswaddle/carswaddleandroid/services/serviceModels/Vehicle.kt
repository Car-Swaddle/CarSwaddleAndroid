package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Vehicle(
    val id: String,
    val licensePlate: String,
    val state: String,
    val name: String,
    val vin: String,
    val createdAt: Date,
    val updatedAt: Date,
    val userID: String,
    val vehicleDescription: VehicleDescription?
)



//"vehicle": {
//    "id": "5da74bc0-358c-11ea-b7c8-7b3292d1eb24",
//    "licensePlate": "C968HK",
//    "state": "UT",
//    "name": "Ford Edge",
//    "vin": null,
//    "createdAt": "2020-01-12T22:39:17.116Z",
//    "updatedAt": "2020-01-12T22:39:17.116Z",
//    "userID": "35669110-ba13-11e9-b6de-45ab229113ae",
//    "vehicleDescription": null
//},