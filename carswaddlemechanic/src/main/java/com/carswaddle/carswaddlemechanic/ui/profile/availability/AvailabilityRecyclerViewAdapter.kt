package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.ui.TimeAvailabilityItem


/// Displays a list of the week with time slots on each day
class AvailabilityRecyclerViewAdapter() : RecyclerView.Adapter<DayAvailabilityViewHolder>() {
    
    fun updateSelectedTimeSlots(timeAvailabilityItems: Set<TimeAvailabilityItem>) {
        this.originalWeekdaySelectedTimeSlots = createWeekdaySelectedTimeSlots(timeAvailabilityItems)
        notifyDataSetChanged()
    }
    
    fun getAllSelectedTimeSlots(): Set<TimeAvailabilityItem> {
        var s: MutableSet<TimeAvailabilityItem> = mutableSetOf()
        for (weekday in Weekday.all()) {
            s.addAll(adjustedWeekdaySelectedTimeSlots[weekday] ?: setOf())
        }
        return s
    }
    
    /// This is a list of all the selected time slots
//    private var selectedTimeSlots: MutableList<TimeAvailabilityItem> = mutableListOf()
//        set(newValue) {
//            field = newValue
//            weekdaySelectedTimeSlots = createWeekdaySelectedTimeSlots(selectedTimeSlots)
//            notifyDataSetChanged()
//        }

    /// The originally set selected times broken up by day of the week
    private var originalWeekdaySelectedTimeSlots: Map<Weekday, Set<TimeAvailabilityItem>> = mapOf()
    set(newValue) {
        field = newValue
        adjustedWeekdaySelectedTimeSlots = originalWeekdaySelectedTimeSlots.toMutableMap()
    }
    /// The set of selected time slots adjsuted by the user
    private var adjustedWeekdaySelectedTimeSlots: MutableMap<Weekday, Set<TimeAvailabilityItem>> = mutableMapOf()
    /// The available times broken up by day of the week
    private var weekdayTimeAvailability: Map<Weekday, List<TimeAvailabilityItem>> = createWeekdayTimeAvailabilityItems()
    
    private fun createWeekdayTimeAvailabilityItems(): Map<Weekday, List<TimeAvailabilityItem>> {
        val minutesInSeconds = 60
        val secondsInADay = 60*60*24

        var map: MutableMap<Weekday, List<TimeAvailabilityItem>> = mutableMapOf()

        for (weekday in Weekday.all()) {
            for (seconds in 0..secondsInADay-1 step 15*minutesInSeconds) {
                val weekdayList = map[weekday]?.toMutableList() ?: mutableListOf()
                weekdayList.add(TimeAvailabilityItem(weekday, seconds))
                map[weekday] = weekdayList
            }
        }
        return map
    }
    
    private fun createWeekdaySelectedTimeSlots(selectedTimeSlots: Set<TimeAvailabilityItem>): Map<Weekday, MutableSet<TimeAvailabilityItem>> {
        var map: MutableMap<Weekday, MutableSet<TimeAvailabilityItem>> = mutableMapOf()
        for (slot in selectedTimeSlots) {
            val weekdayList = map[slot.weekday] ?: mutableSetOf()
            weekdayList.add(slot)
            map[slot.weekday] = weekdayList
        }
        return map
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayAvailabilityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_availability_item, parent, false)
        return DayAvailabilityViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: DayAvailabilityViewHolder, position: Int) {
        val weekday = Weekday.fromInt(position)
        holder.configureTimeSlots(weekdayTimeAvailability[weekday] ?: listOf(), originalWeekdaySelectedTimeSlots[weekday] ?: mutableSetOf(), weekday)
        holder.selectedTimeSlotsDidChange = {
            adjustedWeekdaySelectedTimeSlots[weekday] = it
        }
    }

    override fun getItemCount(): Int = Weekday.all().count()
    
}