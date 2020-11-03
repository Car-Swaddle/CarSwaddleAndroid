package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R

class VehicleRecyclerViewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<VehicleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
//        holder.textView.text = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topTextView: TextView = view.findViewById(R.id.top_text)
        val bottomTextView: TextView = view.findViewById(R.id.bottom_text)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}