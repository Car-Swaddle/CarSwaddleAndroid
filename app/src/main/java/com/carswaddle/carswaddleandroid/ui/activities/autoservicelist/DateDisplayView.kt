package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.carswaddle.carswaddleandroid.R
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DateDisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val dayOfMonthTextView: TextView
    val monthTextView: TextView
    val dayOfWeekAndTimeTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.date_display, this, true)

        dayOfMonthTextView = findViewById<TextView>(R.id.dayOfMonth)
        monthTextView = findViewById<TextView>(R.id.month)
        dayOfWeekAndTimeTextView = findViewById<TextView>(R.id.dayAndTime)
    }

    fun configure(calendar: Calendar) {
        dayOfMonthTextView.text = SimpleDateFormat("dd").format(calendar.getTime())
        monthTextView.text = SimpleDateFormat("MMM").format(calendar.getTime())
        dayOfWeekAndTimeTextView.text = dayOfWeekAndTime(calendar)
    }

    private fun dayOfWeekAndTime(calendar: Calendar): String {
        val symbols = DateFormatSymbols(Locale.getDefault())
        symbols.setAmPmStrings(arrayOf("am", "pm"))
        return SimpleDateFormat("EEE hh:mm aa", symbols).format(calendar.getTime())
    }

}