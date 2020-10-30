package com.carswaddle.carswaddleandroid.ui.activities.schedule

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan

class TimePickerRecyclerViewAdapter(
) : RecyclerView.Adapter<TimePickerRecyclerViewAdapter.ViewHolder>() {
    
    var timeSlots: List<TemplateTimeSpan> = listOf()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }
    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_time_slot, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder
//        val item = values[position]
//        // TODO - set image
//        holder.nameTextView.text = item.name
//        holder.ratingBar.rating = item.averageRating
//        val roundedValue = Math.round(item.averageRating * 10) / 10.0
//        holder.ratingTextView.text = "$roundedValue avg from ${item.ratingCount} ratings"
//        holder.servicesCompletedTextView.text = "${item.servicesCompleted} services completed"
        val slot = timeSlots[position]
        holder.timeTextView.text = slot.localizedStartTime()
    }
    
    override fun getItemCount(): Int = timeSlots.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        
//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}