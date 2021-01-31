package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan


class DayAvailabilityViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var timeAvailabilityItems: List<TimeAvailabilityItem> = listOf()
    private var selectedTimeSlots: List<TemplateTimeSpan> = listOf()
    set(newValue) {
        field = newValue
        selectedTimeSlotsDidChange(selectedTimeSlots)
    }
    
    var selectedTimeSlotsDidChange: (selectedTimeSlots: List<TemplateTimeSpan>) -> Unit = {}

    fun configureTimeSlots(timeAvailabilityItems: List<TimeAvailabilityItem>, selectedTimeSlots: List<TemplateTimeSpan>) {
        this.timeAvailabilityItems = timeAvailabilityItems
        this.selectedTimeSlots = selectedTimeSlots
    }
    
}

