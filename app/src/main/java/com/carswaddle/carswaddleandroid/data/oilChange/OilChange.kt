package com.carswaddle.carswaddleandroid.data.oilChange

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import java.util.*

@Entity
data class OilChange (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "oil_type") val oilType: OilType,
    @ColumnInfo(name = "created_at") val createdAt: Date?,
    @ColumnInfo(name = "updated_at") val updatedAt: Date?) {

    constructor(oilChange: com.carswaddle.carswaddleandroid.services.serviceModels.OilChange) :
            this(oilChange.id,
            oilChange.oilType,
            oilChange.createdAt,
            oilChange.updatedAt)

}
