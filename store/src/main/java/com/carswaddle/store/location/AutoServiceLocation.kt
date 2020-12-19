package com.carswaddle.carswaddleandroid.data.location

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceLocation
import com.google.android.gms.maps.model.LatLng
import java.util.*


@Entity
data class AutoServiceLocation(
    @PrimaryKey val id: String,
    @ColumnInfo val streetAddress: String?,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val createdAt: Calendar,
    @ColumnInfo val updatedAt: Calendar
){
    constructor(location: com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceLocation) :
            this(location.id, location.streetAddress, location.point.latitude(), location.point.longitude(), location.createdAt.toCalendar(), location.updatedAt.toCalendar())

    val location: Location
        get() {
            val loc = Location("")
            loc.latitude = latitude
            loc.longitude = longitude
            return loc
        }


    val latLong: LatLng
        get() {
            return LatLng(latitude, longitude)
        }

}
