package com.carswaddle.carswaddleandroid.data.vehicle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
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
    @ColumnInfo val licensePlate: String,
    @ColumnInfo val state: String?,
    @ColumnInfo val name: String?,
    @ColumnInfo val vin: String?,
    @ColumnInfo val createdAt: Calendar,
    @ColumnInfo val updatedAt: Calendar,
    @ColumnInfo val userID: String?,
    @ColumnInfo val vehicleDescriptionId: String?
) {

    constructor(vehicle: com.carswaddle.carswaddleandroid.services.serviceModels.Vehicle) :
            this(vehicle.id,
                vehicle.licensePlate,
                vehicle.state,
                vehicle.name,
                vehicle.vin,
                vehicle.createdAt.toCalendar(),
                vehicle.updatedAt.toCalendar(),
                vehicle.userID,
                vehicle.vehicleDescription?.id
            )
}