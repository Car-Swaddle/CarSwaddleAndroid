package com.carswaddle.carswaddleandroid.data

import androidx.room.TypeConverter
import java.util.*

class CalendarConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let { value ->
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = value
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}