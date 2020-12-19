package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RatingBar
import androidx.appcompat.widget.AppCompatRatingBar
import com.carswaddle.carswaddleandroid.R

class ReviewCreationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {
    
    val reviewEditText: EditText
    val serviceRatingBar: RatingBar

    init {
        LayoutInflater.from(context).inflate(R.layout.view_review_creation, this, true)
        
        reviewEditText = findViewById(R.id.editTextReview)
        serviceRatingBar = findViewById(R.id.rateServiceBar)
    }
    
}