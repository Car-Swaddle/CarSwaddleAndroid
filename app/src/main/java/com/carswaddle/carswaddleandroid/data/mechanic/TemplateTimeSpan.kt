package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
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
//        var startTimeCal = Calendar.getInstance()
//        val hourOfDay = startTime / 60 / 60 
//        val minuteInHour = startTime / 60 % 60
//        val secondInMinute = startTime % 60
//        startTimeCal.set(Calendar.HOUR_OF_DAY, hourOfDay)
//        startTimeCal.set(Calendar.SECOND, secondInMinute)
//        startTimeCal.set(Calendar.MINUTE, minuteInHour)
        
        val startTimeCal = calendar
        
        val sdf = SimpleDateFormat("h:mm a")
        
        val symbols = DateFormatSymbols(Locale.getDefault())
        symbols.setAmPmStrings(arrayOf("am", "pm"))
        sdf.setDateFormatSymbols(symbols)
        
        return sdf.format(startTimeCal.getTime())
    }
    
    val calendar: Calendar
    get() {
        var startTimeCal = Calendar.getInstance()
        val hourOfDay = startTime / 60 / 60
        val minuteInHour = startTime / 60 % 60
        val secondInMinute = startTime % 60
        startTimeCal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        startTimeCal.set(Calendar.SECOND, secondInMinute)
        startTimeCal.set(Calendar.MINUTE, minuteInHour)
        
        return startTimeCal
    }
    
}


private fun Int.secondsToMilliseconds(): Int {
    return this * 1000
}
