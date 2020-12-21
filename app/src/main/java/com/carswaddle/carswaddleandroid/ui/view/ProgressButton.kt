package com.carswaddle.carswaddleandroid.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.activities.schedule.details.VehicleRecyclerViewAdapter
import com.google.android.material.button.MaterialButton


class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    val button: MaterialButton
    val progressBar: ProgressBar

    // The text that will be displayed when `isLoading` is false
    var displayText: String = "" 
        set(newValue) { 
            field = newValue
            
            if (isLoading == false) {
                button.text = displayText
            }
        }
    
    
    var isLoading: Boolean = false 
        set(newValue) { 
            field = newValue
            
            // disable button when isLoading is true
            isButtonEnabled = !newValue
            
            if (newValue == true) {
                button.text = null
                progressBar.visibility = VISIBLE
            } else {
                button.text = displayText
                progressBar.visibility = GONE
            }
        }
    
    var isButtonEnabled: Boolean = true 
        get() = button.isEnabled
        set(newValue) { 
            field = newValue
            button.isEnabled = newValue 
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.view_progress_button)
        val styleInt = a.getInt(R.styleable.view_progress_button_progressButtonStyle, 0)
        val styleEnum = ProgressButtonStyle.fromInt(styleInt)
        var layout = 0
        when(styleEnum) {
            ProgressButtonStyle.FILLED_BRAND -> layout = R.layout.view_progress_button
            ProgressButtonStyle.FILLED_ALTERNATE -> layout = R.layout.view_progress_button_alternate
            ProgressButtonStyle.BORDERLESS -> layout = R.layout.view_progress_button_borderless
            ProgressButtonStyle.OUTLINE -> layout = R.layout.view_progress_button_outline
        }
        
        LayoutInflater.from(context).inflate(layout, this, true)
        
        button = findViewById(R.id.progressButton)
        progressBar = findViewById(R.id.progressBar)
        
        progressBar.visibility = GONE
        
        val s: CharSequence? = a.getString(R.styleable.view_progress_button_displayText)
        if (s != null) {
            displayText = s as String
        }
        
        val foreground = a.getColorStateList(R.styleable.view_progress_button_progressButtonForegroundColor)
        if (foreground != null) {
            button.setTextColor(foreground)
            progressBar.indeterminateTintList = foreground
        }

        val textColorOverride = a.getColorStateList(R.styleable.view_progress_button_progressButtonTextColor)
        if (textColorOverride != null) {
            button.setTextColor(textColorOverride)
        }
        
        val progressBarColor = a.getColorStateList(R.styleable.view_progress_button_progressBarColor)
        if (progressBarColor != null) {
            progressBar.indeterminateTintList = foreground
        }
    }

}

enum class ProgressButtonStyle(val value: Int) {
    FILLED_BRAND(0),
    FILLED_ALTERNATE(1),
    BORDERLESS(2),
    OUTLINE(3);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}


/*
* <enum name="filledBrand" value="0" />
            <enum name="filledAlternate" value="1" />
            <enum name="borderless" value="2" />
            <enum name="outline" value="3" />
* */