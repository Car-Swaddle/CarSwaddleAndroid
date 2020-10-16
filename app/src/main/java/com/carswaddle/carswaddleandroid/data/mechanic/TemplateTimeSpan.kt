package com.carswaddle.carswaddleandroid.data.mechanic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday

@Entity
data class TemplateTimeSpan(
    @PrimaryKey val id: String,
    @ColumnInfo val weekDayInt: Int,
    @ColumnInfo val startTime: Int,
    @ColumnInfo val duration: Int) {

    constructor(span: com.carswaddle.carswaddleandroid.services.serviceModels.TemplateTimeSpan) :
            this(span.id,
                span.weekDay,
                span.startTimeInt,
                span.duration)
    
    
    fun weekday(): Weekday {
        return Weekday.fromInt(weekDayInt)
    }
    
}
