package com.carswaddle.carswaddleandroid.data.vehicleDescription

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class VehicleDescription(
    @PrimaryKey val id: String,
    @ColumnInfo val make: String,
    @ColumnInfo val model: String,
    @ColumnInfo val style: String,
    @ColumnInfo val trim: String,
    @ColumnInfo val year: String) {

    constructor(vehicleDescription: com.carswaddle.carswaddleandroid.services.serviceModels.VehicleDescription) :
            this(vehicleDescription.id,
            vehicleDescription.make,
            vehicleDescription.model,
            vehicleDescription.style,
            vehicleDescription.trim,
            vehicleDescription.year)

}
