package com.carswaddle.carswaddleandroid.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.carswaddle.carswaddleandroid.R

class PriceRow : FrameLayout {

    private lateinit var priceLabelTextView: TextView
    private lateinit var priceValueTextView: TextView

    var label: String = ""
        set(value) {
            field = value
            priceLabelTextView.text = value
        }

    var value: String = ""
        set(value) {
            field = value
            priceValueTextView.text = value
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
            R.layout.view_price_row, this)
        priceLabelTextView = findViewById(R.id.priceLabel)
        priceValueTextView = findViewById(R.id.priceValue)
    }
}
