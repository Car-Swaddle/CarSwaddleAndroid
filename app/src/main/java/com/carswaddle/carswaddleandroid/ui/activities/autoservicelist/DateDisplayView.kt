package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.carswaddle.carswaddleandroid.R
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
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
        try {
            val localDateTime = LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
            dayOfMonthTextView.text = dayFormatter.format(localDateTime)
            monthTextView.text = monthFormatter.format(localDateTime)
            dayOfWeekAndTimeTextView.text = dayOfWeekAndTimeFormatter.format(localDateTime)
        } catch (e: DateTimeException) {
            Log.e(this.javaClass.simpleName, "Failed to format dates for date display view", e)
        }
    }

    companion object {
        val dayFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("dd")
            .toFormatter(Locale.US)
        val monthFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("MMM")
            .toFormatter(Locale.US)
        val dayOfWeekAndTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("EEE hh:mm ")
            .appendText(ChronoField.AMPM_OF_DAY, mapOf(0L to "am", 1L to "pm"))
            .toFormatter(Locale.US)

    }

}