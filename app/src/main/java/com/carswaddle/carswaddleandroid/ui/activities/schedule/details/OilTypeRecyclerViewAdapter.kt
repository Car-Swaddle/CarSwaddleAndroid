package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType.*
import com.carswaddle.carswaddleandroid.ui.activities.schedule.MyMechanicRecyclerViewAdapter

class OilTypeRecyclerViewAdapter(
    private val context: Context
) : RecyclerView.Adapter<OilTypeRecyclerViewAdapter.ViewHolder>() {

    private val values: List<OilType> = listOf(CONVENTIONAL, BLEND, SYNTHETIC, HIGH_MILEAGE)
    
    var selectedPosition: Int = 1
    
    var selectedIndex: Int
    get() {
        return selectedPosition - 1
    }
    set(newValue) {
        selectedPosition = newValue + 1
    }
    
    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.oil_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) != VIEW_TYPE_ITEM) {
            holder.itemView.alpha = 0f
            return
        }
        holder.itemView.alpha = 1f

        val index = position - 1
        val item = values[index]
        holder.textView.text = item.localizedString()
        if (index == selectedIndex) {
            holder.textView.background = ContextCompat.getDrawable(context, R.drawable.large_selected_border)
        } else {
            holder.textView.background = ContextCompat.getDrawable(context, R.drawable.large_unselected_border)
        }
    }

    override fun getItemCount(): Int = values.size + 2

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
    }

    //http://www.plattysoft.com/2015/06/16/snapping-items-on-a-horizontal-list/
    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == itemCount - 1) {
            VIEW_TYPE_PADDING
        } else VIEW_TYPE_ITEM
    }

    companion object {
        private const val VIEW_TYPE_PADDING = 1
        private const val VIEW_TYPE_ITEM = 2
    }

}