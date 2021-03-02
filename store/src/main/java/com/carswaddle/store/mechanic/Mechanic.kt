package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import java.util.*


@Entity
data class Mechanic(
    @PrimaryKey val id: String,
    @ColumnInfo val isActive: Boolean,
    @ColumnInfo val isAllowed: Boolean,
    @ColumnInfo val chargeForTravel: Boolean,  
    @ColumnInfo val hasSetAvailability: Boolean,
    @ColumnInfo val hasSetServiceRegion: Boolean,
    @ColumnInfo val dateOfBirth: Calendar?,
    @ColumnInfo var profileImageID: String?,
    @ColumnInfo val identityDocumentID: String?,
    @ColumnInfo val identityDocumentBackID: String?,
    @ColumnInfo val userId: String?,
    @ColumnInfo val stripeAccountID: String?,
    @ColumnInfo val scheduleTimeSpanIds: List<String>?,
    @ColumnInfo val serviceIds: List<String>?,
    @ColumnInfo val reviewIds: List<String>?,
    @ColumnInfo val serviceRegionId: String?,
    @ColumnInfo var averageRating: Double?,
    @ColumnInfo var numberOfRatings: Int?,
    @ColumnInfo var autoServicesProvided: Int?) {

    constructor(mechanic: com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic, averageRating: Double?, numberOfRatings: Int?, numberOfServicesProvied: Int?) :
            this(mechanic.id,
                mechanic.isActive,
                mechanic.isAllowed,
                mechanic.chargeForTravel,
                mechanic.hasSetAvailability,
                mechanic.hasSetServiceRegion,
                mechanic.dateOfBirth?.toCalendar(),
                mechanic.profileImageID,
                mechanic.identityDocumentID,
                mechanic.identityDocumentBackID,
                mechanic.userID,
                mechanic.stripeAccountID,
                mechanic.scheduleTimeSpans?.map { it.id },
                mechanic.services?.map { it.id },
                mechanic.reviews?.map { it.id },
                mechanic.serviceRegion?.id,
                mechanic.stats?.averageRating ?: averageRating,
                mechanic.stats?.numberOfRatings ?: numberOfRatings,
                mechanic.stats?.autoServicesProvided ?: numberOfServicesProvied)

}
