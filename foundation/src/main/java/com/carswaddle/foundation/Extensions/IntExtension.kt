package com.carswaddle.foundation.Extensions

import java.util.*

fun Int.centsToDollars(): Float {
    return this.toFloat()/100.0f
}

fun Double.epochToDate(): Date {
    return Date((this*1000).toLong())
}
