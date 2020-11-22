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
    @SerializedName("CONVENTIONAL") conventional,
    @SerializedName("BLEND") blend,
    @SerializedName("SYNTHETIC") synthetic,
    @SerializedName("HIGH_MILEAGE") highMileage;

    fun localizedString(): String {
        when (this) {
            conventional -> return "Conventional oil type"
            blend -> return "Blend oil type"
            synthetic -> return "Synthetic oil type"
            highMileage -> return "High mileage oil type"
            else -> return ""
        }
    }

}