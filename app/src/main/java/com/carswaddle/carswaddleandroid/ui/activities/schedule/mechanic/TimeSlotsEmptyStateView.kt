package com.carswaddle.carswaddleandroid.ui.activities.schedule.mechanic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.carswaddle.carswaddleandroid.R

class TimeSlotsEmptyStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private val textView: TextView
    
    init {
        LayoutInflater.from(context).inflate(R.layout.time_slots_empty_state, this, true)
        
        textView = findViewById(R.id.emptyTimeSlotsTextView)
    }
    
}