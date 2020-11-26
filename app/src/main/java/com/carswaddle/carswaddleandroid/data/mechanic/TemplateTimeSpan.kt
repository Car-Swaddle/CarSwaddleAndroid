package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*

@Entity
data class TemplateTimeSpan(
    @PrimaryKey val id: String,
    @ColumnInfo val weekDayInt: Int,
    /// The number of seconds since midnight. The time the time slot starts
    @ColumnInfo val startTime: Int,
    @ColumnInfo val duration: Float
) {

    constructor(span: com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan) :
            this(
                span.id,
                span.weekDay,
                span.startTimeInt,
                span.duration
            )
    
    
    fun weekday(): Weekday {
        return Weekday.fromInt(weekDayInt)
    }
    
    fun localizedStartTime(): String {
        return dateTimeFormatter.format(localTime)
    }

    val localTime: LocalTime get() {
        val hourOfDay = startTime / 60 / 60
        val minuteInHour = startTime / 60 % 60
        val secondInMinute = startTime % 60
        return LocalTime.of(hourOfDay, minuteInHour, secondInMinute)
    }

    companion object {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("hh:mm a")
            .toFormatter(Locale.US)
    }
    
}


private fun Int.secondsToMilliseconds(): Int {
    return this * 1000
}
