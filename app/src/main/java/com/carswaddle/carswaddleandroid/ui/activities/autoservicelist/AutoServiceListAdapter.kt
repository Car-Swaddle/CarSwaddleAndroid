package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R

class AutoServiceListAdapter(private val autoServices: LiveData<List<AutoServiceListElements>>, val listener: (AutoServiceListElements) -> Unit) :
    RecyclerView.Adapter<AutoServiceViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoServiceViewHolder {
        // create a new view
        val autoservicesListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.autoservices_list_item, parent, false)

        return AutoServiceViewHolder(autoservicesListView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AutoServiceViewHolder, position: Int) {
        val autoService = autoServices.value?.get(position)
        if (autoService != null) {
            holder.configure(autoService, listener)
        }
    }

    override fun getItemCount(): Int {
        return autoServices.value?.count() ?: 0
    }
}

