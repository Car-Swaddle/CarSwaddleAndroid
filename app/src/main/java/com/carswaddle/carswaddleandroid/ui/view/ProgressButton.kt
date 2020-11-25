package com.carswaddle.carswaddleandroid.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.carswaddle.carswaddleandroid.R
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
        LayoutInflater.from(context).inflate(R.layout.view_progress_button, this, true)
        
        button = findViewById(R.id.progressButton)
        progressBar = findViewById(R.id.progressBar)
        
        progressBar.visibility = GONE
        
//        val displayText = attrs.getattribute

        val a = context.obtainStyledAttributes(attrs, R.styleable.view_progress_button)
        val s: CharSequence? = a.getString(R.styleable.view_progress_button_displayText)
        
        if (s != null) {
            displayText = s as String
        }

    }

}