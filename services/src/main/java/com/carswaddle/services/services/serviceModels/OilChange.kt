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
            CONVENTIONAL -> return "Conventional"
            BLEND -> return "Blend"
            SYNTHETIC -> return "Synthetic"
            HIGH_MILEAGE -> return "High mileage"
            else -> return ""
        }
    }

}