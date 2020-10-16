package com.carswaddle.carswaddleandroid.services.serviceModels

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class TemplateTimeSpan(
    val id: String,
    // The day of the week sunday=0,...
    val weekDay: Int,
    /// The second of the day, provided as HH:mm format string
    val startTime: String,
    /// The number of seconds
    val duration: Int,
    val mechanicID: String
) {

    val weekDayEnum: Weekday = Weekday.fromInt(weekDay)

    var startTimeInt: Int = 0
    get() {
        val format = DateTimeFormatter.ofPattern("HH:mm")
        val date = LocalDate.parse(startTime, format)

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        cal.time = sdf.parse(startTime)
        
        val midnight: Calendar = Calendar.getInstance()

        midnight.set(Calendar.HOUR_OF_DAY, 0)
        midnight.set(Calendar.MINUTE, 0)
        midnight.set(Calendar.SECOND, 0)
        midnight.set(Calendar.MILLISECOND, 0)

        val difference: Long = cal.getTimeInMillis() - midnight.getTimeInMillis()
        return difference.toInt()
    }
    
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
