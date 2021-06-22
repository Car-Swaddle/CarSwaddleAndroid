package com.carswaddle.carswaddlemechanic.ui.profile.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.data.Review.Review
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionItem
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionSectionViewHolder
import com.carswaddle.carswaddlemechanic.ui.earnings.transactions.TransactionViewHolder
import com.carswaddle.store.transaction.Transaction
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class ReviewsListAdapter(
    private val reviews: LiveData<List<Review>>,
    val context: Context,
    val listener: (Review) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_review_list_item, parent, false)
        return ReviewViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val review = (reviews.value ?: listOf())[position]
        if (holder is ReviewViewHolder) {
            holder.reviewTextView.text = if (review.text.isNullOrEmpty() == true)
             context.getString(R.string.no_review_given)
            else 
                review.text
            
            holder.reviewTextView.setTextColor(colorForReviewText(review.text))
            
            val localDateTime = LocalDateTime.ofInstant(
                review.creationDate.toInstant(),
                review.creationDate.timeZone.toZoneId()
            )
            holder.dateTextView.text = TransactionViewHolder.dateFormatter.format(localDateTime)
            holder.ratingBar.rating = review.rating
        }
    }
    
    private fun colorForReviewText(reviewText: String): Int {
        val colorId = if (reviewText.isNullOrEmpty()) R.color.text2 else R.color.text
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            context.resources.getColor(colorId, null)
        } else {
            context.resources.getColor(colorId)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_REVIEW
    }

    override fun getItemCount(): Int {
        return reviews.value?.count() ?: 0
    }

    companion object {
        private const val VIEW_TYPE_REVIEW = 0
        val dateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("MMM d, yyyy")
            .toFormatter(Locale.US)
    }


    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ratingBar: AppCompatRatingBar
        val reviewTextView: TextView
        val dateTextView: TextView

        init {
            dateTextView = view.findViewById(R.id.reviewDateTextView)
            ratingBar = view.findViewById(R.id.reviewRatingBar)
            reviewTextView = view.findViewById(R.id.reviewTextTextView)
        }
    }

}