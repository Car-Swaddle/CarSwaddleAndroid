package com.carswaddle.carswaddleandroid.services.serviceModels

data class Price (
    var identifier: String,
    val autoServiceId: String, 
    var oilChangeCost: Double,
    var distanceCost: Double,
    var bookingFee: Double,
    var processingFee: Double,
    var subtotal: Double,
    var taxes: Double,
    var total: Double
)
    
