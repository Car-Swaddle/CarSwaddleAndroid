package com.carswaddle.carswaddleandroid.services.serviceModels

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

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
    var startTimeInt: Int = 0
    get() {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        
        cal.set(0,0,0,0,0,0)
        
        cal.time = sdf.parse(startTime)

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val second = cal.get(Calendar.SECOND)
        
        val sum = (hour*60*60) + (minute*60) + second
        
        return sum
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
