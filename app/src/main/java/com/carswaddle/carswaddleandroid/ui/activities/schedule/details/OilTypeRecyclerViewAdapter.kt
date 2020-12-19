package com.carswaddle.carswaddleandroid.ui.activities.schedule.details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType.*
import com.carswaddle.carswaddleandroid.ui.activities.schedule.MyMechanicRecyclerViewAdapter

class OilTypeRecyclerViewAdapter() : RecyclerView.Adapter<OilTypeRecyclerViewAdapter.ViewHolder>() {

    private val values: List<OilType> = listOf(SYNTHETIC, BLEND, CONVENTIONAL, HIGH_MILEAGE)
    
    var selectedPosition: Int = 1
    set(newValue) {
        val oldValue = field
        field = newValue
        
    }
    
    val selectedOilType: OilType get() {
        val index = selectedPosition - 1
        if (index < 0 || index >= values.size) {
            Log.e(this.javaClass.simpleName, "Invalid selected position")
            return SYNTHETIC
        }
        return values[index]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.oil_type_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) != VIEW_TYPE_ITEM) {
            holder.rootView.visibility = INVISIBLE
            return
        }
        holder.rootView.visibility = VISIBLE

        val index = position - 1
        val item = values[index]
        holder.textView.text = item.localizedString()
        holder.isSelectedOiltype = position == selectedPosition
//        if (position == selectedPosition) {
//            holder.textView.setBackgroundResource(R.drawable.large_selected_border)
//        } else {
//            holder.textView.setBackgroundResource(R.drawable.large_unselected_border)
//        }
    }

    override fun getItemCount(): Int = values.size + 2

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootView: View = view
        val textView: TextView = view.findViewById(R.id.text_view)
        
        var isSelectedOiltype: Boolean = false
        set(newValue) {
            field = newValue
            if (isSelectedOiltype) {
                textView.setBackgroundResource(R.drawable.large_selected_border)
            } else {
                textView.setBackgroundResource(R.drawable.large_unselected_border)
            }
        }
        
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