package com.carswaddle.carswaddleandroid.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.carswaddle.carswaddleandroid.R

class ProgressTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    val textView: TextView
    val progressBar: ProgressBar

    // The text that will be displayed when `isLoading` is false
    var displayText: String = ""
        set(newValue) {
            field = newValue

            if (isLoading == false) {
                textView.text = displayText
            }
        }
    
    var isLoading: Boolean = false
        set(newValue) {
            field = newValue

            // disable button when isLoading is true
            isTextViewEnabled = !newValue

            if (newValue == true) {
                textView.text = null
                progressBar.visibility = VISIBLE
            } else {
                textView.text = displayText
                progressBar.visibility = GONE
            }
        }

    var isTextViewEnabled: Boolean = true
        get() = textView.isEnabled
        set(newValue) {
            field = newValue
            textView.isEnabled = newValue
            
            if (isTextViewEnabled) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.brand))
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.brandDisabled))
            }
            
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.progress_text_view, this, true)

        textView = findViewById(R.id.progressTextView)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = GONE

        val a = context.obtainStyledAttributes(attrs, R.styleable.progress_text_view)
        val s: CharSequence? = a.getString(R.styleable.progress_text_view_text_view_display_text)

        if (s != null) {
            displayText = s as String
        }

    }

}