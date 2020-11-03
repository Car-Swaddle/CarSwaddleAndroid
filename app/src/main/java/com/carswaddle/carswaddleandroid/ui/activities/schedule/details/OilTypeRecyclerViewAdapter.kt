package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R

class OilTypeRecyclerViewAdapter(
    private val values: List<String>
) : RecyclerView.Adapter<OilTypeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.oil_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textView.text = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}