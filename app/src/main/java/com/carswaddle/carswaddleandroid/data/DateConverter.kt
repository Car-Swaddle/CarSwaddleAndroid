package com.carswaddle.carswaddleandroid.data

import androidx.room.TypeConverter
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}


class AutoServiceStatusConverter {

    @TypeConverter
    fun fromAutoServiceStatusOptional(status: AutoServiceStatus?): String? {
        return status?.name
    }

    @TypeConverter
    fun toAutoServiceStatusOptional(value: String?): AutoServiceStatus? {
        if (value != null) {
            return AutoServiceStatus.valueOf(value)
        }
        return null
    }

}