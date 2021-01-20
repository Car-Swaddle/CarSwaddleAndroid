package com.carswaddle.carswaddlemechanic.ui.earnings.transactions

import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddlemechanic.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*


class TransactionSectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    
    var depositDate: Calendar = Calendar.getInstance()
    set(newValue) {
        field = newValue
        val localDateTime = LocalDateTime.ofInstant(depositDate.toInstant(), depositDate.timeZone.toZoneId())
        val localizedDate = dayOfWeekAndDayOfMonthFormatter.format(localDateTime)
        depositDateTextView.text = "Deposit date $localizedDate"
    }
    
    
    
    private var depositDateTextView: TextView
    
    init {
        depositDateTextView = view.findViewById(R.id.depositDateTextView)
    }
    
    companion object {
        val dayOfWeekAndDayOfMonthFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("MMM, d")
            .toFormatter(Locale.US)
    }
    
}