package com.carswaddle.carswaddleandroid.services.serviceModels

data class Balance(
    val available: Amount,
    val pending: Amount,
    val reserved: Amount?,
    val mechanic: Mechanic?
)