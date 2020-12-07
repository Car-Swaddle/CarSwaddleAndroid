package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.MechanicImageView


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMechanicRecyclerViewAdapter(
) : RecyclerView.Adapter<MyMechanicRecyclerViewAdapter.ViewHolder>() {

    var mechanicElements: List<MechanicListElements> = arrayListOf()
    set(newValue) {
        field = newValue
    }

    init {
        this.mechanicElements = mechanicElements
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mechanic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) != VIEW_TYPE_ITEM) {
            holder.itemView.alpha = 0f
            return
        }
        holder.itemView.alpha = 1f

        val item = mechanicElements[position - 1] // Offset for initial padding
        holder.nameTextView.text = item.user.displayName()
        holder.ratingBar.rating = item.mechanic.averageRating?.toFloat() ?: 0.0F
        holder.mechanicImageView.mechanicId = item.mechanic.id
        
        var roundedValue = "-"
        if (item.mechanic.averageRating != null) {
            roundedValue = (Math.round(
                (item.mechanic.averageRating?.toFloat() ?: 0.0F) * 10
            ) / 10.0).toString()
        }
        val ssb = SpannableStringBuilder().append(
            roundedValue,
            StyleSpan(Typeface.BOLD),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
            .append(" avg from ")
            .append(
                item.mechanic.numberOfRatings?.toString() ?: "0",
                StyleSpan(Typeface.BOLD),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            .append(" ratings")
        holder.ratingTextView.text = ssb

        val ssb2 = SpannableStringBuilder().append(
            item.mechanic.autoServicesProvided?.toString() ?: "0",
            StyleSpan(Typeface.BOLD),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
            .append(" services completed")
        holder.servicesCompletedTextView.text = ssb2
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
        val mechanicImageView: MechanicImageView = view.findViewById(R.id.mechanicImageView)
    }

    companion object {
        private const val VIEW_TYPE_PADDING = 1
        private const val VIEW_TYPE_ITEM = 2
    }

}

