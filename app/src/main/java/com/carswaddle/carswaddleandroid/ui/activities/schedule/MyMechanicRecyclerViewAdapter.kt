package com.carswaddle.carswaddleandroid.ui.activities.schedule

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import com.carswaddle.carswaddleandroid.R

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyMechanicRecyclerViewAdapter(
    private val values: List<Mechanic>
) : RecyclerView.Adapter<MyMechanicRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mechanic_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        // TODO - set image
        holder.nameTextView.text = item.name
        holder.ratingBar.rating = item.averageRating
        val roundedValue = Math.round(item.averageRating * 10) / 10.0
        holder.ratingTextView.text = "$roundedValue avg from ${item.ratingCount} ratings"
        holder.servicesCompletedTextView.text = "${item.servicesCompleted} services completed"
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.name)
        val ratingBar: AppCompatRatingBar = view.findViewById(R.id.ratingBar)
        val ratingTextView: TextView = view.findViewById(R.id.ratings)
        val servicesCompletedTextView: TextView = view.findViewById(R.id.services_completed)

//        override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
//        }
    }

    public class Mechanic() {

        val id: Int = 0
        val name: String = "Johnny Appleseed"
        val averageRating: Float = 4.755f
        val ratingCount: Int = 8
        val servicesCompleted: Int = 20
    }
}