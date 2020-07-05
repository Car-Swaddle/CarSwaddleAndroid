package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Mechanic(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "isActive") val isActive: Boolean,
    @ColumnInfo(name = "isAllowed") val isAllowed: Boolean,
    @ColumnInfo(name = "userId") val userId: String?,
    @ColumnInfo(name = "scheduleTimeSpanIds") val scheduleTimeSpanIds: List<String>,
    @ColumnInfo(name = "serviceIds") val serviceIds: List<String>,
    @ColumnInfo(name = "reviewIds") val reviewIds: List<String>?,
    @ColumnInfo(name = "serviceRegionId") val serviceRegionId: String?,
    @ColumnInfo(name = "averateRating") val averateRating: Double?,
    @ColumnInfo(name = "numberOfRatings") val numberOfRatings: Int?,
    @ColumnInfo(name = "autoServicesProvided") val autoServicesProvided: Int?) {

    constructor(mechanic: com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic) :
            this(mechanic.identifier,
                mechanic.isActive,
                mechanic.isAllowed,
                mechanic.user?.id,
                mechanic.scheduleTimeSpans.map { it.identifier },
                mechanic.services.map { it.id },
                mechanic.reviews.map { it.id },
                mechanic.serviceRegion?.identifier,
                mechanic.stats?.averageRating,
                mechanic.stats?.numberOfRatings,
                mechanic.stats?.autoServicesProvided)

}



