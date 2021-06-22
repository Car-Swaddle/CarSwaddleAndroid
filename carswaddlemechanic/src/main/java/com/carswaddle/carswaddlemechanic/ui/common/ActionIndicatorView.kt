package com.carswaddle.carswaddlemechanic.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.carswaddle.carswaddlemechanic.R

class ActionIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {
    
    init {
        LayoutInflater.from(context).inflate(R.layout.view_action_indicator, this, true)
    }
    
}