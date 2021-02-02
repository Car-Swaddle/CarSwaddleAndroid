package com.carswaddle.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.services.serviceModels.Weekday
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


/// Represents slots that are available to be shown 
data class TimeAvailabilityItem(
    val weekday: Weekday,
    val secondsSinceMidnight: Int
) {
    val localTime: LocalTime
        get() {
            val hourOfDay = secondsSinceMidnight / 60 / 60
            val minuteInHour = secondsSinceMidnight / 60 % 60
            val secondInMinute = secondsSinceMidnight % 60
            return LocalTime.of(hourOfDay, minuteInHour, secondInMinute)
        }
}

class TimeItemSelectionViewAdapter(
) : RecyclerView.Adapter<TimeItemSelectionViewAdapter.ViewHolder>() {

    var allowMultiselect: Boolean = false

    var timeItems: List<TimeAvailabilityItem> = listOf()
        set(newValue) {
            field = newValue
            removeSelection()
            notifyDataSetChanged()
        }

    var originalSelectedTimeItems: Set<TimeAvailabilityItem> = setOf()
        set(newValue) {
            field = newValue
            currentSelectedTimeItems = newValue.toMutableSet()
        }

    private var currentSelectedTimeItems: MutableSet<TimeAvailabilityItem> = mutableSetOf()

    var didChangeSelectedTimeItems: (selectedTimeSlots: Set<TimeAvailabilityItem>) -> Unit =
        { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_time_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = timeItems[position]
        holder.timeTextView.text = localizedDate(item)
        holder.isSelectedView = currentSelectedTimeItems.contains(item)

        holder.itemView.setOnClickListener() {
            val item = timeItems[position]

            if (allowMultiselect) {
                if (currentSelectedTimeItems.contains(item)) {
                    currentSelectedTimeItems.remove(item)
                } else {
                    currentSelectedTimeItems.add(item)
                }
                notifyItemChanged(position)
            } else {
                if (currentSelectedTimeItems.contains(item) == false) {
                    removeSelection()
                    currentSelectedTimeItems.add(item)
                    notifyDataSetChanged()
                }
            }

            didChangeSelectedTimeItems(currentSelectedTimeItems)
        }
    }

    private fun localizedDate(timeItem: TimeAvailabilityItem): String {
        return dateTimeFormatter.format(timeItem.localTime)
    }

    private fun removeSelection() {
        currentSelectedTimeItems = mutableSetOf()
    }

    override fun getItemCount(): Int = timeItems.size

    companion object {
        val dateTimeFormatter: DateTimeFormatter
            get() {
                return DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("h:mm a")
                    .toFormatter(Locale.US)
            }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)

        var isSelectedView: Boolean = false
            set(newValue) {
                field = newValue
                if (isSelectedView) {
                    timeTextView.setBackgroundColor(
                        ResourcesCompat.getColor(
                            itemView.resources,
                            R.color.brand,
                            null
                        )
                    )
                } else {
                    timeTextView.setBackgroundColor(
                        ResourcesCompat.getColor(
                            itemView.resources,
                            R.color.content,
                            null
                        )
                    )
                }
                timeTextView.setTextColor(textColor())
            }

        private fun textColor(): Int {
            if (isSelectedView) {
                return ResourcesCompat.getColor(itemView.resources, R.color.brandContrast, null)
            } else {
                return ResourcesCompat.getColor(itemView.resources, R.color.text, null)
            }
        }

    }

}

@SuppressLint("toZoneId is bad because of some API version thing")
fun Date.toInstant(): LocalDateTime? {
    return LocalDateTime.ofInstant(
        this.toInstant(),
        this.toCalendar().timeZone.toZoneId()
    )
}