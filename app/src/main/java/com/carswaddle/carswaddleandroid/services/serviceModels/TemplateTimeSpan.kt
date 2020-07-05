package com.carswaddle.carswaddleandroid.services.serviceModels

data class TemplateTimeSpan (
    val identifier: String,
    /// The second of the day
    val startTime: Int,
    /// The number of seconds
    val duration: Int,
    val mechanic: Mechanic
)