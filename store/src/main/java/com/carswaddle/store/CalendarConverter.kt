package com.carswaddle.store

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





class CalendarDoubleConverter {

    @TypeConverter
    fun fromTimestamp(value: Double?): Calendar? = value?.let { value ->
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = value.toLong()
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Double? = timestamp?.timeInMillis?.toDouble()
}


class CalendarIntConverter {

    @TypeConverter
    fun fromTimestamp(value: Int?): Calendar? = value?.let { value ->
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = value.toLong()
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Int? = timestamp?.timeInMillis?.toInt()
}