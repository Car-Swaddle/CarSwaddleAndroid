package com.carswaddle.carswaddlemechanic.ui.calendar.singleday

import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class DayAutoServiceSectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    
    var date: Calendar = Calendar.getInstance()
    set(newValue) {
        field = newValue
        val localDateTime = LocalDateTime.ofInstant(newValue.toInstant(), newValue.timeZone.toZoneId())
        sectionHeaderTextView.text = dayOfWeekAndDayOfMonthFormatter.format(localDateTime)
        
        if (DateUtils.isToday(date.timeInMillis)) {
            todayTextView.text = "Today"
            todayTextView.visibility = View.VISIBLE
            spacerTextView.visibility = View.VISIBLE
        } else {
            // TODO: iOS app shows "tomorrow" too
            todayTextView.visibility = View.GONE
            spacerTextView.visibility = View.GONE
        }
    }
    
    var displayConnectingLine: Boolean = true
    set(newValue) {
        field = newValue
        headerConnectingView.visibility = if (displayConnectingLine) View.VISIBLE else View.GONE
    }
    
    private var sectionHeaderTextView: TextView
    private var spacerTextView: TextView
    private var todayTextView: TextView
    private var headerConnectingView: View
    
    
    init {
        sectionHeaderTextView = view.findViewById(R.id.sectionHeaderTextView)
        spacerTextView = view.findViewById(R.id.sectionHeaderSpacer)
        todayTextView = view.findViewById(R.id.sectionHeaderTodayTextView)
        headerConnectingView = view.findViewById(R.id.headerConnectingView)
    }
    
    companion object {
        val dayOfWeekAndDayOfMonthFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("EEEE, d")
            .toFormatter(Locale.US)
    }
    
}