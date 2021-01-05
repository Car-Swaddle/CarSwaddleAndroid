package com.carswaddle.carswaddlemechanic.extensions

import com.carswaddle.carswaddleandroid.Extensions.addDays
import com.carswaddle.carswaddleandroid.Extensions.toPickerCalendar
import com.haibin.calendarview.Calendar
import java.util.*

// Only days from tomorrow until one week from now are valid
fun Calendar.isWithinDaysOfToday(daysForwardStart: Int = 1, daysForwardEnd: Int = 7): Boolean {
    return this >= today().addDays(daysForwardStart) && this <= today().addDays(daysForwardEnd)
}

fun Calendar.today(): Calendar {
    val javaCalendar = java.util.Calendar.getInstance()
    javaCalendar.time = Date()
    return javaCalendar.toPickerCalendar()
}
