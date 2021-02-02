package com.carswaddle.carswaddlemechanic.ui.profile.availability

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.ui.TimeAvailabilityItem
import com.carswaddle.ui.TimeItemSelectionViewAdapter


class DayAvailabilityViewHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {

    private var timeAvailabilityItems: List<TimeAvailabilityItem> = listOf()
    set(newValue) {
        field = newValue
        viewAdapter.timeItems = timeAvailabilityItems.toMutableList()
    }
    var selectedTimeSlotsDidChange: (selectedTimeSlots: Set<TimeAvailabilityItem>) -> Unit = {}

    fun configureTimeSlots(timeAvailabilityItems: List<TimeAvailabilityItem>, selectedTimeSlots: Set<TimeAvailabilityItem>, weekday: Weekday) {
        this.timeAvailabilityItems = timeAvailabilityItems
        viewAdapter.originalSelectedTimeItems = selectedTimeSlots
        viewAdapter.notifyDataSetChanged()
        weekdayTitleTextView.text = weekday.localizedStringSentenceCase()
    }
    
    private var recyclerView: RecyclerView
    private var viewAdapter: TimeItemSelectionViewAdapter
    private var viewManager: RecyclerView.LayoutManager
    private var weekdayTitleTextView: TextView
    
    init {
        weekdayTitleTextView = view.findViewById(R.id.weekdayTitleTextView)
        viewAdapter = TimeItemSelectionViewAdapter()
        viewAdapter.allowMultiselect = true
        viewAdapter.didChangeSelectedTimeItems = { 
            selectedTimeSlotsDidChange(it)
        }
        viewManager = LinearLayoutManager(context)
        

        recyclerView = view.findViewById(R.id.dayAvailabilityRecyclerView)
        recyclerView.adapter = viewAdapter
        recyclerView.setLayoutManager(object: GridLayoutManager(context, 4) {
            // override methods here
        })
    }
    
}

fun List<TemplateTimeSpan>.toTimeAvailabilityItems(): List<TimeAvailabilityItem> {
    val mutableList: MutableList<TimeAvailabilityItem> = mutableListOf()
    for (s in this) {
        mutableList.add(TimeAvailabilityItem(s.weekday(), s.startTime))
    }
    return mutableList
}

fun Set<TemplateTimeSpan>.toTimeAvailabilityItems(): Set<TimeAvailabilityItem> {
    val mutableSet: MutableSet<TimeAvailabilityItem> = mutableSetOf()
    for (s in this) {
        mutableSet.add(TimeAvailabilityItem(s.weekday(), s.startTime))
    }
    return mutableSet.toSet()
}

fun MutableSet<TemplateTimeSpan>.toMutableTimeAvailabilityItems(): MutableSet<TimeAvailabilityItem> {
    val mutableSet: MutableSet<TimeAvailabilityItem> = mutableSetOf()
    for (s in this) {
        mutableSet.add(TimeAvailabilityItem(s.weekday(), s.startTime))
    }
    return mutableSet
}
