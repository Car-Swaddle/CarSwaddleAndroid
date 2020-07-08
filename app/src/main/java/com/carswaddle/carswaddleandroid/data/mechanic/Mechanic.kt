package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Mechanic(
    @PrimaryKey val id: String,
    @ColumnInfo val isActive: Boolean,
    @ColumnInfo val isAllowed: Boolean,
    @ColumnInfo val userId: String?,
    @ColumnInfo val scheduleTimeSpanIds: List<String>?,
    @ColumnInfo val serviceIds: List<String>?,
    @ColumnInfo val reviewIds: List<String>?,
    @ColumnInfo val serviceRegionId: String?,
    @ColumnInfo val averageRating: Double?,
    @ColumnInfo val numberOfRatings: Int?,
    @ColumnInfo val autoServicesProvided: Int?) {

    constructor(mechanic: com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic) :
            this(mechanic.id,
                mechanic.isActive,
                mechanic.isAllowed,
                mechanic.userID,
                mechanic.scheduleTimeSpans?.map { it.identifier },
                mechanic.services?.map { it.id },
                mechanic.reviews?.map { it.id },
                mechanic.serviceRegion?.identifier,
                mechanic.stats?.averageRating,
                mechanic.stats?.numberOfRatings,
                mechanic.stats?.autoServicesProvided)

}



