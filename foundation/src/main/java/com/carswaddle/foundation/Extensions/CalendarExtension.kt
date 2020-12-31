package com.carswaddle.carswaddleandroid.Extensions

import java.util.*

//fun Calendar.toDate(): Date {
//    return this.getTime()
//}

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.setTime(this)
    return calendar
}