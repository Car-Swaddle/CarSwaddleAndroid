package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListSectionViewHolder.*

class AutoServiceListAdapter(private val upcomingServices: LiveData<List<AutoServiceListElements>>, val pastServices: LiveData<List<AutoServiceListElements>>, val listener: (AutoServiceListElements) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    var currentUser: User? = null

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        if (viewType == VIEW_TYPE_UPCOMING_HEADER || viewType == VIEW_TYPE_PAST_HEADER) {
            val autoservicesListView = LayoutInflater.from(parent.context)
                .inflate(R.layout.autoservices_section_header, parent, false)
            return AutoServiceListSectionViewHolder(autoservicesListView)
        } else {
            val autoservicesListView = LayoutInflater.from(parent.context)
                .inflate(R.layout.autoservices_list_item, parent, false)

            return AutoServiceViewHolder(autoservicesListView)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (holder is AutoServiceListSectionViewHolder) {
            if (viewType == VIEW_TYPE_UPCOMING_HEADER) {
                holder.configure(SectionType.UPCOMING, upcomingServices.value?.count() ?: 0)
            } else if (viewType == VIEW_TYPE_PAST_HEADER) {
                holder.configure(SectionType.PAST, pastServices.value?.count() ?: 0)
            }
            holder.currentUsername = currentUser?.firstName
        } else if (holder is AutoServiceViewHolder) {
            val autoService = this.autoService(position)
            holder.configure(autoService, listener)
        } 
    }

    private fun autoService(position: Int): AutoServiceListElements {
        val upcomingCount = upcomingServices.value?.count() ?: 0
        val autoService: AutoServiceListElements
        if (upcomingCount > 0 && position <= upcomingCount) {
            val l = upcomingServices.value ?: listOf()
            autoService = l[position-1]
        } else {
            if (position < upcomingCount+1) {
                val l = upcomingServices.value ?: listOf()
                return l[position-1]
            } else {
                val l = pastServices.value ?: listOf()
                val adjustedPosition = position - upcomingCount
                return l[adjustedPosition-1]
            }
        }
        return autoService
    }
    
    override fun getItemViewType(position: Int): Int {
        val upcomingCount = upcomingServices.value?.count() ?: 0
        
        if (upcomingCount > 0) {
            if (position == 0) {
                return VIEW_TYPE_UPCOMING_HEADER
            } else if (position > 0 && position <= upcomingCount) {
                return VIEW_TYPE_UPCOMING_SERVICE
            } else if (position == upcomingCount+1) {
                return VIEW_TYPE_PAST_HEADER
            } else {
                return VIEW_TYPE_PAST_SERVICE
            }
        } else {
            if (position == 0) {
                return VIEW_TYPE_PAST_HEADER
            } else {
                return VIEW_TYPE_PAST_SERVICE
            }
        }
    }

    override fun getItemCount(): Int {
        val upcomingCount = upcomingServices.value?.count() ?: 0
        val pastCount = pastServices.value?.count() ?: 0
        
        if (upcomingCount > 0) {
            return 1 + upcomingCount + 1 + pastCount  
        } else {
            return 1 + pastCount
        }
    }
    
    companion object {
        private const val VIEW_TYPE_UPCOMING_HEADER = 0
        private const val VIEW_TYPE_UPCOMING_SERVICE = 1
        private const val VIEW_TYPE_PAST_HEADER = 2
        private const val VIEW_TYPE_PAST_SERVICE = 3
    }
    
}

