package com.carswaddle.carswaddleandroid.data.vehicle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//data class Vehicle(
//    val id: String,
//    val licensePlate: String,
//    val state: String,
//    val name: String,
//    val vin: String,
//    val createdAt: Date,
//    val updatedAt: Date,
//    val userID: String,
//    val vehicleDescription: VehicleDescription?
//)



@Entity
data class Vehicle(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "licensePlate") val licensePlate: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "vin") val vin: String,
    @ColumnInfo(name = "createdAt") val createdAt: Calendar,
    @ColumnInfo(name = "updatedAt") val updatedAt: Calendar,
    @ColumnInfo(name = "userID") val userID: String?,
    @ColumnInfo(name = "vehicleDescriptionId") val vehicleDescriptionId: String?) {

    constructor(vehicle: com.carswaddle.carswaddleandroid.services.serviceModels.Vehicle) :
            this(vehicle.id,
                vehicle.licensePlate,
                vehicle.state,
                vehicle.name,
                vehicle.vin,
                vehicle.createdAt,
                vehicle.updatedAt,
                vehicle.userID,
                vehicle.vehicleDescription?.id)
}