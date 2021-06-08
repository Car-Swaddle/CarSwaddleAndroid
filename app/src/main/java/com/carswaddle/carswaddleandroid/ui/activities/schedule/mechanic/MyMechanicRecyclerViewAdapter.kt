package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicListElements
import com.carswaddle.carswaddleandroid.services.serviceModels.OilType
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.MechanicImageView


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMechanicRecyclerViewAdapter(
) : RecyclerView.Adapter<MyMechanicRecyclerViewAdapter.ViewHolder>() {

    var mechanicElements: List<MechanicListElements> = arrayListOf()

    var selectedPosition: Int = 1

    val selectedMechanicListElements: MechanicListElements? get() {
        val index = selectedPosition - 1
        if (index < 0 || index >= mechanicElements.size) {
            Log.e(this.javaClass.simpleName, "Invalid selected position")
            return null
        }
        return mechanicElements[index]
    }

    val selectedMechanicId: String? get() {
        return selectedMechanicListElements?.mechanic?.id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mechanic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) != VIEW_TYPE_ITEM) {
            holder.itemView.alpha = 0f
            holder.itemView.layoutParams.width = Resources.getSystem().displayMetrics.widthPixels / 3
            return
        }
        holder.itemView.alpha = 1f
        holder.itemView.layoutParams.width = WRAP_CONTENT

        val item = mechanicElements[position - 1] // Offset for initial padding
        holder.nameTextView.text = item.user.displayName()
        holder.ratingBar.rating = item.mechanic.averageRating?.toFloat() ?: 0.0F
        holder.mechanicImageView.mechanicId = item.mechanic.id
        
        val price = item.oilChangePricing?.synthetic
        if (price != null) {
            holder.priceTextView.text = String.format("$%.2f", price / 100.0)
        } else {
            holder.priceTextView.text = "$--"
        }
        
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


        holder.isSelected = position == selectedPosition
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
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        val ratingTextView: TextView = view.findViewById(R.id.ratings)
        val servicesCompletedTextView: TextView = view.findViewById(R.id.services_completed)
        val mechanicImageView: MechanicImageView = view.findViewById(R.id.mechanicImageView)
        val rootView: View = view

        var isSelected: Boolean = false
            set(newValue) {
                field = newValue
                if (isSelected) {
                    rootView.setBackgroundResource(R.drawable.large_selected_border)
                } else {
                    rootView.setBackgroundResource(R.drawable.large_unselected_border)
                }
            }
    }

    companion object {
        private const val VIEW_TYPE_PADDING = 1
        private const val VIEW_TYPE_ITEM = 2
    }

}

