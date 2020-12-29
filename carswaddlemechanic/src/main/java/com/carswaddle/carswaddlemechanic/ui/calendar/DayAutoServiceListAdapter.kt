package com.carswaddle.carswaddlemechanic.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddlemechanic.R
import java.util.*

class DayAutoServiceListAdapter(private val services: LiveData<List<AutoServiceListElements>>, val listener: (AutoServiceListElements) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    var date: Calendar = Calendar.getInstance()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }
    
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.autoservices_section_header, parent, false)
            return DayAutoServiceSectionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.autoservices_list_item, parent, false)

            return DayAutoServiceViewHolder(view)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (holder is DayAutoServiceSectionViewHolder) {
            holder.date = date
        } else if (holder is DayAutoServiceViewHolder) {
            val s = services.value
            if (s != null) {
                holder.configure(s[position-1], listener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_SERVICE
    }

    override fun getItemCount(): Int {
        val s = services.value
        if (s == null) {
            return 0
        }
        return if (s.count() == 0) 0 else s.count() + 1
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_SERVICE = 1
    }

}