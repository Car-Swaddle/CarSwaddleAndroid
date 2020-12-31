package com.carswaddle.carswaddleandroid.services.serviceModels

import android.R.attr
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*


data class UpdateAutoService(
    val status: String?,
    val notes: String?,
    val vehicleID: String?,
    val mechanicID: String?,
    val locationID: String?,
    val scheduledDate: Date?,
    val location: ServerLocation?,
    val review: CreateReview?
)



data class CreateAutoService(
    val status: AutoServiceStatus,
    val notes: String?,
    val vehicleID: String,
    val mechanicID: String,
    val scheduledDate: Date,
    val locationID: String?,
    val location: ServerLocation?,
    val serviceEntities: List<CreateServiceEntity>,
    val sourceID: String
) {
    companion object {
        fun oilChangeServiceEntities(oilType: OilType): List<CreateServiceEntity> {
            val oilChange = CreateServiceEntity.init(oilType)
            return listOf(oilChange)
        }
    }
}

data class ServerLocation(
    val longitude: Double,
    val latitude: Double,
    val streetAddress: String?
)

data class CreateReview(
    val rating: Float,
    val text: String 
)
