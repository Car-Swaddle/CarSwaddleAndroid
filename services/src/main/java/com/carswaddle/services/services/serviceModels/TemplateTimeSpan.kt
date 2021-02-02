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

data class UpdateTemplateTimeSpan(
    val weekDay: Int,
    val startTime: Int,
    val duration: Float,
)

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
        fun fromInt(value: Int) = values().first { it.value == value }
        fun all(): List<Weekday> {
            return listOf(sunday, monday, tuesday, wednesday, thursday, friday, saturday)
        }
    }

    fun localizedString(): String {
        when (this) {
            sunday -> return "sunday"
            monday -> return "monday"
            tuesday -> return "tuesday"
            wednesday -> return "wednesday"
            thursday -> return "thursday"
            friday -> return "friday"
            saturday -> return "saturday"
            else -> return ""
        }
    }

    fun localizedStringSentenceCase(): String {
        when (this) {
            sunday -> return "Sunday"
            monday -> return "Monday"
            tuesday -> return "Tuesday"
            wednesday -> return "Wednesday"
            thursday -> return "Thursday"
            friday -> return "Friday"
            saturday -> return "Saturday"
            else -> return ""
        }
    }

}
