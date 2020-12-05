package com.carswaddle.carswaddleandroid.ui.activities.schedule

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.carswaddle.carswaddleandroid.Extensions.safeFirst
import com.carswaddle.carswaddleandroid.Extensions.safeObject
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan

class TimePickerRecyclerViewAdapter(
) : RecyclerView.Adapter<TimePickerRecyclerViewAdapter.ViewHolder>() {
    
    var timeSlots: List<TemplateTimeSpan> = listOf()
        set(newValue) {
            field = newValue
            selectedIndex = null
            notifyDataSetChanged()
        }
    
    var didChangeSelectedTimeSlot:  (selectedTimeSlot: TemplateTimeSpan?) -> Unit = { _ -> }
    
    val selectedTimeSlot: TemplateTimeSpan?
    get() {
        val i = selectedIndex
        if (i == null) {
            return null
        }
        return timeSlots.safeObject(i)
    }
    
    var selectedIndex: Int? = null
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
        didChangeSelectedTimeSlot(selectedTimeSlot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_time_slot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val slot = timeSlots[position]
        holder.timeTextView.text = slot.localizedStartTime()
        holder.isSelectedView = selectedIndex == position
        
        holder.itemView.setOnClickListener() {
            selectedIndex = position
            notifyItemChanged(position)
        }
    }
    
    override fun getItemCount(): Int = timeSlots.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        
        var isSelectedView: Boolean = false
        set(newValue) {
            field = newValue
            if (isSelectedView) {
                timeTextView.setBackgroundColor(ResourcesCompat.getColor(itemView.resources, R.color.brand, null))
            } else {
                timeTextView.setBackgroundColor(ResourcesCompat.getColor(itemView.resources, R.color.content, null))
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