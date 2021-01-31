package com.carswaddle.carswaddleandroid.services.serviceModels

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

data class TemplateTimeSpan(
    val id: String,
    // The day of the week sunday=0,...
    val weekDay: Int,
    /// The second of the day, provided as HH:mm format string
    val startTime: String,
    /// The number of seconds
    val duration: Float,
    val mechanicID: String
) {

    val weekDayEnum: Weekday = Weekday.fromInt(weekDay)

    /// Start time of the time span in seconds since midnight
    val startTimeInt: Int
    get() {
        val localTime = LocalTime.parse(startTime, dateTimeFormatter)
        return localTime.toSecondOfDay()
    }

    companion object {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("HH:mm:ss")
            .toFormatter(Locale.US)
    }
    
}

private fun Long.millisecondsToSeconds(): Long {
    return this / 1000
}


enum class Weekday(val value: Int) {
    sunday(0),
    monday(1),
    tuesday(2),
    wednesday(3),
    thursday(4),
    friday(5),
    saturday(6);

    companion object {
        fun fromInt(value: Int) = Weekday.values().first { it.value == value }
        fun all(): List<Weekday> {
            return listOf(sunday, monday, tuesday, wednesday, thursday, friday, saturday)
        }
    }

    fun localizedString(): String {
        when (this) {
            Weekday.sunday -> return "sunday"
            Weekday.monday -> return "monday"
            Weekday.tuesday -> return "tuesday"
            Weekday.wednesday -> return "wednesday"
            Weekday.thursday -> return "thursday"
            Weekday.friday -> return "friday"
            Weekday.saturday -> return "saturday"
            else -> return ""
        }
    }

}
