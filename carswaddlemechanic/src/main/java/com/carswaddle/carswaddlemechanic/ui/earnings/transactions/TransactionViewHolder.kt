package com.carswaddle.carswaddlemechanic.ui.earnings.transactions

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
import com.carswaddle.store.transaction.Transaction
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*


class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val dateTextView: TextView
    private val statusTextView: TextView
    private val descriptionTextView: TextView
    private val priceTextView: TextView

    val context = view.context

    fun configure(
        transaction: Transaction,
        listener: (Transaction) -> Unit
    ) {
        val localDateTime = LocalDateTime.ofInstant(
            transaction.created.toInstant(),
            transaction.created.timeZone.toZoneId()
        )
        dateTextView.text = dateFormatter.format(localDateTime)
        statusTextView.text = transaction.type.localizedString()
        val desc = transaction.transactionDescription
        descriptionTextView.text = desc
        if (desc == null) {
            descriptionTextView.visibility = View.GONE
        } else {
            descriptionTextView.visibility = View.VISIBLE
        }

        val dollars = transaction.amount.centsToDollars()
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
            listener(transaction)
        }
    }

    init {
        dateTextView = view.findViewById(R.id.transactionDateTextView)
        statusTextView = view.findViewById(R.id.transactionStatusTextView)
        descriptionTextView = view.findViewById(R.id.transactionDescriptionTextView)
        priceTextView = view.findViewById(R.id.priceTextView)
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

fun TransactionType.localizedString(): String {
    return when (this) {
        TransactionType.adjustment -> "Adjustment"
        TransactionType.advance -> "Advance"
        TransactionType.advanceFunding -> "Advance funding"
        TransactionType.applicationFee -> "Application fee"
        TransactionType.applicationFeeRefund -> "Application fee refund"
        TransactionType.charge -> "Charge"
        TransactionType.connectCollectionTransfer -> "Connect collection transfer"
        TransactionType.issuingAuthorizationHold -> "Issuing authorization hold"
        TransactionType.issuingAuthorizationRelease -> "Issuing authorization release"
        TransactionType.issuingTransaction -> "Issuing transaction"
        TransactionType.payment -> "Payment"
        TransactionType.paymentFailureRefund -> "Payment failure refund"
        TransactionType.paymentRefund -> "Payment refund"
        TransactionType.payout -> "Payout"
        TransactionType.payoutCancel -> "Payout cancel"
        TransactionType.payoutFailure -> "Payout failure"
        TransactionType.refund -> "Refund"
        TransactionType.refundFailure -> "Refund failure"
        TransactionType.reserveTransaction -> "Reserve transaction"
        TransactionType.reservedFunds -> "Reserved funds"
        TransactionType.stripeFee -> "Stripe fee"
        TransactionType.stripeFxFee -> "Stripe fx fee"
        TransactionType.taxFee -> "Tax fee"
        TransactionType.topup -> "Topup"
        TransactionType.topupReversal -> "Topup reversal"
        TransactionType.transfer -> "Transfer"
        TransactionType.transferCancel -> "Transfer cancel"
        TransactionType.transferFailure -> "Transfer failure"
        TransactionType.transferRefund -> "Transfer refund"
    }
}
