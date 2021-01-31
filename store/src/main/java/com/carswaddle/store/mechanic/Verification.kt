package com.carswaddle.store.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic
import com.carswaddle.carswaddleandroid.services.serviceModels.Verification as ServiceVerification
import java.util.*


@Entity
data class Verification(
    @PrimaryKey val id: String,
    @ColumnInfo val disabledReason: String?,
    @ColumnInfo val dueByDate: Calendar?,
    @ColumnInfo val mechanicId: String?,
    @ColumnInfo val pastDue: List<String>?,
    @ColumnInfo val currentlyDue: List<String>?,
    @ColumnInfo val eventuallyDue: List<String>?

) {
    constructor(verification: ServiceVerification, mechanicId: String) :
            this(
                "verification" + mechanicId,
                verification.disabledReason,
                verification.dueByDate,
                mechanicId,
                verification.pastDue,
                verification.currentlyDue,
                verification.eventuallyDue,
            )

}
