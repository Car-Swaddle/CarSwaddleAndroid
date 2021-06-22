package com.carswaddle.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat


class ProgressTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    val textView: TextView
    val progressBar: ProgressBar

    var enabledTextColor: ColorStateList? = null
        set(newValue) {
            field = newValue
            updateTextColor()
        }

    var disabledTextColor: ColorStateList? = null
        set(newValue) {
            field = newValue
            updateTextColor()
        }

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
            updateTextColor()
        }

    private fun updateTextColor() {
        if (isTextViewEnabled) {
            val t = enabledTextColor
            if (t == null) {
                textView.setTextColor(
                    ContextCompat.getColor(context, R.color.common_google_signin_btn_text_dark)
                )
            } else {
                textView.setTextColor(t)
            }

        } else {
            val t = disabledTextColor
            if (t == null) {
                textView.setTextColor(
                    ContextCompat.getColor(context, R.color.common_google_signin_btn_text_dark_disabled)
                )
            } else {
                textView.setTextColor(t)
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.progress_text_view, this, true)

        textView = findViewById(R.id.progressTextView)
        progressBar = findViewById(R.id.progressBar)

        val a = context.obtainStyledAttributes(attrs, R.styleable.progress_text_view)
        val enabledColor =
            a.getColorStateList(R.styleable.progress_text_view_text_view_enabled_text_color)
        if (enabledColor != null) {
            enabledTextColor = enabledColor
        }

        val disabledColor =
            a.getColorStateList(R.styleable.progress_text_view_text_view_disabled_text_color)
        if (disabledColor != null) {
            disabledTextColor = disabledColor
        }
        
        progressBar.visibility = GONE

        val s: CharSequence? = a.getString(R.styleable.progress_text_view_text_view_display_text)

        if (s != null) {
            displayText = s as String
        }
        
        updateTextColor()
    }

}