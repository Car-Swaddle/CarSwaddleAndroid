package com.carswaddle.foundation.Extensions

import java.util.*


private const val metersToMilesConstant: Float = 1609.34f

fun Int.centsToDollars(): Float {
    return this.toFloat()/100.0f
}

fun Int.metersToMiles(): Float {
    return this.toFloat() / metersToMilesConstant
}

fun Double.metersToMiles(): Double {
    return this / metersToMilesConstant
}

fun Double.epochToDate(): Date {
    return Date((this*1000).toLong())
}
