package com.carswaddle.carswaddleandroid.services.serviceModels

import java.util.*

data class Verification (
    val disabledReason: String?,
    val dueByDate: Calendar?,
    val mechanic: Mechanic?,
    val pastDue: List<String>,
    val currentlyDue: List<String>,
    val eventuallyDue: List<String>
)