package com.carswaddle.store

import androidx.room.TypeConverter
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
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

class OilTypeConverter {

    @TypeConverter
    fun fromOilTypeStatusOptional(status: OilType?): String? {
        return status?.name
    }

    @TypeConverter
    fun toOilTypeOptional(value: String?): OilType? {
        if (value != null) {
            return OilType.valueOf(value)
        }
        return null
    }

}


class WeekdayConverter {

    @TypeConverter
    fun fromWeekdayOptional(weekDay: Weekday): String {
        return weekDay.value.toString()
    }

    @TypeConverter
    fun toWeekdayOptional(value: String): Weekday {
//        if (value != null) {
            return Weekday.valueOf(value)
//        }
//        return null
    }

}