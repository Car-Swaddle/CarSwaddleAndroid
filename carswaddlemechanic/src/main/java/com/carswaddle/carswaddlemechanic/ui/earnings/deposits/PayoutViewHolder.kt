package com.carswaddle.carswaddlemechanic.ui.earnings.deposits

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.ImageLabel
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.services.serviceModels.TransactionType
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.application.CarSwaddleMechanicApp.Companion.applicationContext
import com.carswaddle.carswaddlemechanic.ui.common.AutoserviceStatusView
import com.carswaddle.foundation.Extensions.centsToDollars
import com.carswaddle.store.payout.Payout
import com.carswaddle.store.transaction.Transaction
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*


class PayoutViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val dateTextView: TextView
    private val statusTextView: TextView
    private val descriptionTextView: TextView
    private val priceTextView: TextView
    private val errorTextView: TextView
    private val statementTextView: TextView

    val context = view.context

    fun configure(
        payout: Payout,
        listener: (Payout) -> Unit
    ) {
        val localDateTime = LocalDateTime.ofInstant(
            payout.arrivalDate.toInstant(),
            payout.arrivalDate.timeZone.toZoneId()
        )
        dateTextView.text = dateFormatter.format(localDateTime)
        statusTextView.text = payout.status.localizedString()
        val desc = payout.payoutDescription
        descriptionTextView.text = desc
        if (desc == null) {
            descriptionTextView.visibility = View.GONE
        } else {
            descriptionTextView.visibility = View.VISIBLE
        }
        
        val statement = payout.statementDescriptor
        statementTextView.text = statement
        statementTextView.visibility = if (statement == null) View.GONE else View.VISIBLE

        val failMessage = payout.failureMessage
        errorTextView.text = failMessage
        errorTextView.visibility = if (failMessage == null) View.GONE else View.VISIBLE

        val dollars = payout.amount.centsToDollars()
        priceTextView.text = currencyFormatter.format(dollars)

        var colorResource: Int = R.color.text

        if (dollars > 0) {
            colorResource = R.color.success
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            priceTextView.setTextColor(context.resources.getColor(colorResource, null))
        } else {
            priceTextView.setTextColor(context.resources.getColor(colorResource))
        }

        itemView.setOnClickListener {
            listener(payout)
        }
    }

    init {
        dateTextView = view.findViewById(R.id.payoutDateTextView)
        statusTextView = view.findViewById(R.id.payoutStatusTextView)
        descriptionTextView = view.findViewById(R.id.payoutDescriptionTextView)
        priceTextView = view.findViewById(R.id.payoutPriceTextView)
        statementTextView = view.findViewById(R.id.payoutStatementTextView)
        errorTextView = view.findViewById(R.id.payoutErrorTextView)
    }

    companion object {
        val dateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("MMM d, yyyy")
            .toFormatter(Locale.US)
        val currencyFormatter: NumberFormat by lazy {
            val f = NumberFormat.getCurrencyInstance()
            f.minimumFractionDigits = 2
            f.maximumFractionDigits = 2
            return@lazy f
        }
    }

}
