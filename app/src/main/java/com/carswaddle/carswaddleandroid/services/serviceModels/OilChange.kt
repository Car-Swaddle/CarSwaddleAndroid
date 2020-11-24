package com.carswaddle.carswaddleandroid.services.serviceModels

import com.google.gson.annotations.SerializedName
import java.util.*

data class OilChange(
    val id: String,
    val oilType: OilType,
    val createdAt: Date?,
    val updatedAt: Date?
)

data class UploadOilChange(
    val oilType: OilType
)

enum class OilType {
    CONVENTIONAL,
    BLEND,
    SYNTHETIC,
    HIGH_MILEAGE;

    fun localizedString(): String {
        when (this) {
            CONVENTIONAL -> return "Conventional oil type"
            BLEND -> return "Blend oil type"
            SYNTHETIC -> return "Synthetic oil type"
            HIGH_MILEAGE -> return "High mileage oil type"
            else -> return ""
        }
    }

}