package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMechanicRecyclerViewAdapter(
) : RecyclerView.Adapter<MyMechanicRecyclerViewAdapter.ViewHolder>() {
    
    var mechanicElements: List<MechanicListElements> = arrayListOf()
    set(newValue) {
        field = newValue
        notifyDataSetChanged()
    }
    
    init {
        this.mechanicElements = mechanicElements
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

//        return if (viewType == VIEW_TYPE_ITEM) {
//            val v: View = LayoutInflater.from(parent.context)
//                .inflate(R.layout.list_item, parent, false)
//            ViewHolder(v)
//        } else {
//            val v: View = LayoutInflater.from(parent.context)
//                .inflate(R.layout.list_item_padding, parent, false)
//            ViewHolder(v)
//        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mechanic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) != VIEW_TYPE_ITEM) {
            // TOOD - reset everything or set alpha to 0
            holder.itemView.alpha = 0f
            return
        }
        holder.itemView.alpha = 1f
        val item = mechanicElements[position - 1] // Offset for initial padding
        // TODO - set image
        holder.nameTextView.text = item.user.displayName()
        holder.ratingBar.rating = item.mechanic.averageRating?.toFloat() ?: 0.0F
        val roundedValue = Math.round(item.mechanic.averageRating?.toFloat() ?: 0.0F).times(10).div(
            10
        ).toFloat()
        holder.ratingTextView.text = "$roundedValue avg from ${item.mechanic.numberOfRatings} ratings"
        holder.servicesCompletedTextView.text = "${item.mechanic.autoServicesProvided} services completed"
    }

    override fun getItemCount(): Int = mechanicElements.size + 2 // Padding at start and end

    //http://www.plattysoft.com/2015/06/16/snapping-items-on-a-horizontal-list/
    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == itemCount - 1) {
            VIEW_TYPE_PADDING
        } else VIEW_TYPE_ITEM
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name)
        val ratingBar: AppCompatRatingBar = view.findViewById(R.id.ratingBar)
        val ratingTextView: TextView = view.findViewById(R.id.ratings)
        val servicesCompletedTextView: TextView = view.findViewById(R.id.services_completed)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

    companion object {
        private const val VIEW_TYPE_PADDING = 1
        private const val VIEW_TYPE_ITEM = 2
    }

}

