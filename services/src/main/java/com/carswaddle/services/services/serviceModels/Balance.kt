package com.carswaddle.carswaddleandroid.services.serviceModels

data class Balance(
    val available: List<Amount>,
    val pending: List<Amount>,
    val connect_reserved: List<Amount>?
)