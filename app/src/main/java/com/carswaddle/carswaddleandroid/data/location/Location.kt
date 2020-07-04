package com.carswaddle.carswaddleandroid.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Location
import java.util.*


@Entity
data class Location(
    @PrimaryKey val id: String,
    @ColumnInfo val streetAddress: String,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val createdAt: Calendar,
    @ColumnInfo val updatedAt: Calendar
){
    constructor(location: Location) :
            this(location.id, location.streetAddress, location.point.latitude(), location.point.latitude(), location.createdAt, location.updatedAt)
}
