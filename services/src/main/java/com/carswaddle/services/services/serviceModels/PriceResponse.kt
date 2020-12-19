package com.carswaddle.services.services.serviceModels

import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceLocation


data class PriceResponse (
    val prices: Price,
    val location: AutoServiceLocation
)