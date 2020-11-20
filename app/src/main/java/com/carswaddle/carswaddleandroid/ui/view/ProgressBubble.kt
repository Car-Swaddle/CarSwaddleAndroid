package com.carswaddle.carswaddleandroid.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.carswaddle.carswaddleandroid.R
import com.google.android.material.card.MaterialCardView

class ProgressBubble : FrameLayout {

    private lateinit var cardView: MaterialCardView
    private lateinit var textView: TextView

    private var _stepNumber: Int = 0
    private var _state: ProgressState =
        ProgressState.Active

    enum class ProgressState {
        Active, Complete, Inactive
    }

    var stepNumber: Int
        get() = _stepNumber
        set(value) {
            _stepNumber = value
            textView.text = value.toString()
        }

    var state: ProgressState
        get() = _state
        set(value) {
            _state = value
            when (value) {
                ProgressState.Active -> {
                    cardView.strokeColor = Color.parseColor("#ffffff")
                    cardView.setCardBackgroundColor(Color.parseColor("#BFEC2322"))
                    textView.setTextColor(Color.parseColor("#ffffff"))
                }
                ProgressState.Complete -> {
                    cardView.strokeColor = Color.parseColor("#00000000")
                    cardView.setCardBackgroundColor(Color.parseColor("#E63B7DFF"))
                    textView.setTextColor(Color.parseColor("#ffffff"))
                }
                ProgressState.Inactive -> {
                    cardView.strokeColor = Color.parseColor("#00000000")
                    cardView.setCardBackgroundColor(Color.parseColor("#B3999999"))
                    textView.setTextColor(Color.parseColor("#000000"))
                }
            }
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context,
            R.layout.progress_bubble, this)
        cardView = findViewById(R.id.cardView)
        textView = findViewById(R.id.textView)
    }
}
