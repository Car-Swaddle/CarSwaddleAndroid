package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R


class AutoServiceListSectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    
    var currentUsername: String? = null
    
    private var sectionHeaderTextView: TextView
    
    fun configure(sectionType: SectionType, numberOfServices: Int) {
        when (sectionType) {
            SectionType.UPCOMING -> {
                val u = currentUsername
                if (u == null) {
                    sectionHeaderTextView.text = itemView.context.getResources().getQuantityString(R.plurals.welcome_message_no_name, numberOfServices, numberOfServices)
                } else {
                    sectionHeaderTextView.text = itemView.context.getResources().getQuantityString(R.plurals.welcome_message_name, numberOfServices, numberOfServices, u)
                }
            }
            SectionType.PAST -> {
                sectionHeaderTextView.text = itemView.context.getString(R.string.completed_appointments)
            }
        }
    }
    
    init {
        sectionHeaderTextView = view.findViewById(R.id.sectionHeaderTextView)
    }
    
    enum class SectionType {
        UPCOMING,
        PAST
    }
    
}