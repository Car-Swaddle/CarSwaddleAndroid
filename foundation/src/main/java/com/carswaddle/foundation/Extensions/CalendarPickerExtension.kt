package com.carswaddle.carswaddleandroid.Extensions

import com.haibin.calendarview.Calendar
import java.util.*
import java.util.Calendar.*

fun Calendar.toJavaCalendar(): java.util.Calendar {
    var cal = getInstance()
    cal.clear()
    cal.set(this.year, this.month - 1, this.day)
    return cal
}

fun Calendar.addDays(days: Int): Calendar {
    val javaCalendar = this.toJavaCalendar()
    javaCalendar.add(DATE, days)
    return javaCalendar.toPickerCalendar()
}

fun java.util.Calendar.toPickerCalendar(): Calendar {
    val tomorrow = Calendar()
    tomorrow.year = this.get(YEAR)
    tomorrow.month = this.get(MONTH) + 1
    tomorrow.day = this.get(DATE)
    return tomorrow
}

// Only days from tomorrow until one week from now are valid
fun Calendar.isWithinDaysOfToday(daysForwardStart: Int = 1, daysForwardEnd: Int = 7): Boolean {
    return this >= today().addDays(daysForwardStart) && this <= today().addDays(daysForwardEnd)
}

fun Calendar.today(): Calendar {
    val javaCalendar = getInstance()
    javaCalendar.time = Date()
    return javaCalendar.toPickerCalendar()
}
