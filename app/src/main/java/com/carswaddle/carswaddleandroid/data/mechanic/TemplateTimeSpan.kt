package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormatterBuilder

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
        return dateTimeFormatter.print(localTime)
    }

    val localTime: LocalTime get() {
        return LocalTime.fromMillisOfDay(startTime.secondsToMilliseconds().toLong())
    }

    companion object {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("hh:mm a")
            .toFormatter()
    }
    
}


private fun Int.secondsToMilliseconds(): Int {
    return this * 1000
}
