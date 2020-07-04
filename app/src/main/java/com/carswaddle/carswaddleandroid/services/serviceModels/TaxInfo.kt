package com.carswaddle.carswaddleandroid.services.serviceModels

data class TaxInfo (
    val year: String,
    val metersDriven: Int,
    val mechanicCostInCents: Int,
    val mechanic: Mechanic
)