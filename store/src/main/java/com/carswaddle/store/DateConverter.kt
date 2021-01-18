package com.carswaddle.store

import androidx.room.TypeConverter
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.PayoutStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import com.carswaddle.foundation.Extensions.epochToDate
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


class DateDoubleConverter {

    @TypeConverter
    fun fromTimestamp(value: Double?): Date? {
        val v = value
        if (v != null) {
            return v.epochToDate()
        }
        return null
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Double? {
        return date?.time?.toDouble()
    }

}

class DateIntConverter {

    @TypeConverter
    fun fromTimestamp(value: Int?): Date? {
        val d = value?.toLong()
        if (d != null) {
            return Date(d)
        }
        return null
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Int? {
        return date?.time?.toInt()
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


class PayoutStatusConverter {

    @TypeConverter
    fun fromPayoutStatusOptional(status: PayoutStatus): String {
        return status.name
    }

    @TypeConverter
    fun toPayoutStatusOptional(value: String): PayoutStatus {
        if (value != null) {
            return PayoutStatus.valueOf(value)
        }
        return PayoutStatus.pending
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