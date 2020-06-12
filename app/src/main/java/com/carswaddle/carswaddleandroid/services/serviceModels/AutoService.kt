package com.carswaddle.carswaddleandroid.services.serviceModels

import android.app.Service
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
    val status: String?,
    val transferID: String?,
    val userID: String?,
    val mechanicID: String,
    val vehicleID: String?,
    val serviceEntities: Array<ServiceEntity>,
    val vehicle: Vehicle,
    val reviewFromUser: Review?,
    val reviewFromMechanic: Review?
)
