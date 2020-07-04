package com.carswaddle.carswaddleandroid.services.serviceModels

import android.app.Service
import java.util.*

data class AutoService (
    val id: String,
    val balanceTransactionID: String?,
    val chargeID: String?,
    val couponID: String?,
    val createdAt: Calendar,
    val updatedAt: Calendar,
    val isCanceled: Boolean?,
    val notes: String?,
    val scheduledDate: Calendar?,
    val status: String?,
    val transferID: String?,
    val userID: String?,
    val mechanicID: String,
    val location: Location,
    val vehicleID: String?,
    val vehicle: Vehicle,
    val serviceEntities: Array<ServiceEntity>,
    val reviewFromUser: Review?,
    val reviewFromMechanic: Review?
)
