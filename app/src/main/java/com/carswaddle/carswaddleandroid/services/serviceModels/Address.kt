package com.carswaddle.carswaddleandroid.services.serviceModels

data class Address (
    var identifier: String,
    var line1: String?,
    var line2: String?,
    var postalCode: String?,
    var city: String?,
    var state: String?,
    var country: String?
)