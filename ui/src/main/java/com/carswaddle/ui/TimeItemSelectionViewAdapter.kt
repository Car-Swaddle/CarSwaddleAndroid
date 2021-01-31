package com.carswaddle.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.Extensions.toCalendar
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class TimeItemSelectionViewAdapter(
) : RecyclerView.Adapter<TimeItemSelectionViewAdapter.ViewHolder>() {

    var allowMultiselect: Boolean = false

    var timeItems: MutableList<TemplateTimeSpan> = mutableListOf()
        set(newValue) {
            field = newValue
            removeSelection()
            notifyDataSetChanged()
        }

    var selectedTimeItems: MutableSet<TemplateTimeSpan> = mutableSetOf()

    var didChangeSelectedTimeItems: (selectedTimeSlots: Set<TemplateTimeSpan>) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_time_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = timeItems[position]
        holder.timeTextView.text = localizedDate(item)
        holder.isSelectedView = selectedTimeItems.contains(item)

        holder.itemView.setOnClickListener() {
            val item = timeItems[position]

            if (allowMultiselect) {
                if (selectedTimeItems.contains(item)) {
                    selectedTimeItems.remove(item)
                } else {
                    selectedTimeItems.add(item)
                }
            } else {
                if (selectedTimeItems.contains(item) == false) {
                    selectedTimeItems = mutableSetOf()
                    selectedTimeItems.add(item)
                }
            }

            didChangeSelectedTimeItems(selectedTimeItems.toSet())
            notifyDataSetChanged()
        }
    }


    private fun localizedDate(timeItem: TemplateTimeSpan): String {
        return dateTimeFormatter.format(timeItem.localTime)
    }

    private fun removeSelection() {
        selectedTimeItems = mutableSetOf()
    }

    override fun getItemCount(): Int = timeItems.size

    companion object {
        val dateTimeFormatter: DateTimeFormatter
            get() {
                return DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("hh:mm a")
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


@SuppressLint("toZoneId is bad because of some API thing")
fun Date.toInstant(): LocalDateTime? {
    return LocalDateTime.ofInstant(
        this.toInstant(),
        this.toCalendar().timeZone.toZoneId()
    )
}