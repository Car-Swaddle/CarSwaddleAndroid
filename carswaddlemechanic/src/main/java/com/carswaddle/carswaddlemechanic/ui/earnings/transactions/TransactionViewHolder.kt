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

//    private val oilTypeImageLabel: ImageLabel
//    private val locationImageLabel: ImageLabel
//    private val vehicleImageLabel: ImageLabel
//    private val userImageLabel: ImageLabel
//    private val statusImageView: ImageView
//    private val timeTextView: TextView
//    private val connectingViewFull: View
//    private val connectingViewMid: View
//    private val statusView: AutoserviceStatusView
//    private val lottieAnimationView: View
    
    private val dateTextView: TextView
    private val statusTextView: TextView
    private val descriptionTextView: TextView
    private val priceTextView: TextView

    val context = view.context

    fun configure(
        transaction: Transaction,
        listener: (Transaction) -> Unit
    ) {
        val localDateTime = LocalDateTime.ofInstant(transaction.created.toInstant(), transaction.created.timeZone.toZoneId())
        dateTextView.text = dateFormatter.format(localDateTime)
        statusTextView.text = transaction.status
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
//        
//        val status = autoServiceElements.autoService.status ?: AutoServiceStatus.scheduled
//        statusView.autoServiceStatus = status
//        
//        if (scheduledDate != null) {
//            val localDateTime = LocalDateTime.ofInstant(scheduledDate.toInstant(), scheduledDate.timeZone.toZoneId())
//            timeTextView.text = timeFormatter.format(localDateTime)
//        }
    }

//    private fun statusColorStateList(status: AutoServiceStatus): ColorStateList? {
//        val colorId = when (status) {
//            AutoServiceStatus.scheduled -> R.color.statusColorScheduled
//            AutoServiceStatus.canceled -> R.color.statusColorCanceled
//            AutoServiceStatus.inProgress -> R.color.statusColorInProgress
//            AutoServiceStatus.completed -> R.color.statusColorCompleted
//        }
//        return ContextCompat.getColorStateList(applicationContext, colorId)
//    }
//
//    private fun statusImage(status: AutoServiceStatus): Int? {
//        return when (status) {
//            AutoServiceStatus.scheduled -> R.drawable.ic_calendar_filled
//            AutoServiceStatus.canceled -> R.drawable.ic_x
//            AutoServiceStatus.inProgress -> null
//            AutoServiceStatus.completed -> R.drawable.ic_checkmark
//        }
//    }

    init {
//        val oil: ImageLabel = view.findViewById(R.id.oilTypeImageLabel)
//        oil.imageType = ImageLabel.ImageType.OIL
//        oilTypeImageLabel = oil
//
//        val lImageLabel: ImageLabel = view.findViewById(R.id.addressImageLabel)
//        lImageLabel.imageType = ImageLabel.ImageType.LOCATION
//        locationImageLabel = lImageLabel
//
//        val vLabel: ImageLabel = view.findViewById(R.id.vehicleImageLabel)
//        vLabel.imageType = ImageLabel.ImageType.VEHICLE
//        vehicleImageLabel = vLabel
//
//        val uLabel: ImageLabel = view.findViewById(R.id.userImageLabel)
//        uLabel.imageType = ImageLabel.ImageType.PERSON
//        userImageLabel = uLabel
//
//        statusImageView = view.findViewById(R.id.statusImageView)
//        timeTextView = view.findViewById(R.id.timeTextView)
//        connectingViewFull = view.findViewById(R.id.connectingViewFull)
//        connectingViewMid = view.findViewById(R.id.connectingViewMid)
//        statusView = view.findViewById(R.id.statusView)
//        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
//
//        updateForLastServiceOfDay()
        dateTextView = view.findViewById(R.id.transactionDateTextView)
        statusTextView = view.findViewById(R.id.transactionStatusTextView)
        descriptionTextView = view.findViewById(R.id.transactionDescriptionTextView)
        priceTextView = view.findViewById(R.id.priceTextView)
//        priceTextView.setTypeface(Typeface.MONOSPACE)
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