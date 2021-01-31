package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import com.carswaddle.carswaddlemechanic.R


private const val numberOfDaysInWeek: Int = 7

data class TimeAvailabilityItem (
    val weekday: Weekday,
    val secondsSinceMidnight: Int
)

/// Displays a list of the week with time slots on each day
class AvailabilityRecyclerViewAdapter() : RecyclerView.Adapter<DayAvailabilityViewHolder>() {
    
    /// This is a list of all the selected time slots
    var selectedTimeSlots: List<TemplateTimeSpan> = listOf()
        set(newValue) {
            field = newValue
            weekdaySelectedTimeSlots = createWeekdaySelectedTimeSlots(selectedTimeSlots)
            notifyDataSetChanged()
        }

    /// The selected times broken up by day of the week
    private var weekdaySelectedTimeSlots: Map<Weekday, List<TemplateTimeSpan>> = mapOf()
    /// The available times broken up by day of the week
    private var weekdayTimeAvailability: Map<Weekday, List<TimeAvailabilityItem>> = createWeekdayTimeAvailabilityItems()
    
    /// This is called when the selected time slots are changed
    var saveTimeSlots: (selectedTimeSlots: List<TemplateTimeSpan>) -> Unit = {}
    
    private fun createWeekdayTimeAvailabilityItems(): Map<Weekday, List<TimeAvailabilityItem>> {
        val minutesInSeconds = 60*60
        val secondsInADay = 60*60*24

        var map: MutableMap<Weekday, List<TimeAvailabilityItem>> = mutableMapOf()

        for (weekday in Weekday.all()) {
            for (seconds in 0..secondsInADay step 15*minutesInSeconds) {
                val weekdayList = map[weekday]?.toMutableList() ?: mutableListOf()
                weekdayList.add(TimeAvailabilityItem(weekday, seconds))
                map[weekday] = weekdayList
            }
        }
        return map
    }
    
    private fun createWeekdaySelectedTimeSlots(selectedTimeSlots: List<TemplateTimeSpan>): Map<Weekday, List<TemplateTimeSpan>> {
        var map: MutableMap<Weekday, List<TemplateTimeSpan>> = mutableMapOf()
        for (slot in selectedTimeSlots) {
            val weekdayList = map[slot.weekday()]?.toMutableList() ?: mutableListOf()
            weekdayList.add(slot)
            map[slot.weekday()] = weekdayList.toList()
        }
        return map
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayAvailabilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_day_availability_item, parent, false)
        return DayAvailabilityViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayAvailabilityViewHolder, position: Int) {
        holder.configureTimeSlots(weekdayTimeAvailability[Weekday.fromInt(position)] ?: listOf(), weekdaySelectedTimeSlots[Weekday.fromInt(position)] ?: listOf())
        holder.selectedTimeSlotsDidChange = {
            print("time slots changed")
        }
    }

    override fun getItemCount(): Int = Weekday.all().count()
    
}