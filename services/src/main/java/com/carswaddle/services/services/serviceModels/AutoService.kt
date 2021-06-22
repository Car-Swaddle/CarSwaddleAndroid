package com.carswaddle.carswaddleandroid.services.serviceModels

import com.google.gson.annotations.SerializedName
import java.util.*

data class AutoService (
    val id: String,
    val balanceTransactionID: String?,
    val chargeID: String?,
    val couponID: String?,
    val createdAt: Date,
    val updatedAt: Date,
    val isCanceled: Boolean?,
    val notes: String?,
    val scheduledDate: Date?,
    val status: AutoServiceStatus?,
    val transferID: String?,
    val userID: String?,
    val mechanicID: String,
    val mechanic: Mechanic,
    val location: AutoServiceLocation,
    val vehicleID: String?,
    val vehicle: Vehicle,
    val serviceEntities: List<ServiceEntity>,
    val reviewFromUser: Review?,
    val reviewFromMechanic: Review?,
    val user: User?
)

enum class AutoServiceStatus {
    @SerializedName("scheduled") scheduled,
    @SerializedName("canceled") canceled,
    @SerializedName("inProgress") inProgress,
    @SerializedName("completed") completed;

    fun localizedString(): String {
        when (this) {
            scheduled -> return "Scheduled"
            canceled -> return "Canceled"
            inProgress -> return "In progress"
            completed -> return "Completed"
            else -> return ""
        }
    }

}
