package com.carswaddle.store.region

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Region
import com.carswaddle.carswaddleandroid.services.serviceModels.TaxInfo

@Entity
data class Region(
    @PrimaryKey val id: String,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val radius: Double,
) {

    constructor(region: Region) :
            this(
                region.id,
                region.latitude,
                region.longitude,
                region.radius
            )

}