package com.carswaddle.carswaddleandroid.data.serviceEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.services.serviceModels.OilChange
import java.util.*

@Entity
data class ServiceEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "entity_type") val entityType: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "updated_at") val updatedAt: Date,
    @ColumnInfo(name = "auto_service_id") val autoServiceID: String,
    @ColumnInfo(name = "oil_change_id") val oilChangeID: String) {

    constructor(serviceEntity: com.carswaddle.carswaddleandroid.services.serviceModels.ServiceEntity) :
            this(serviceEntity.id,
            serviceEntity.entityType.toString(),
            serviceEntity.createdAt,
            serviceEntity.updatedAt,
            serviceEntity.autoServiceID,
            serviceEntity.oilChangeID)

}
