package com.carswaddle.carswaddlemechanic.ui.calendar.singleday

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.application.CarSwaddleMechanicApp.Companion.applicationContext
import com.carswaddle.carswaddlemechanic.ui.common.ImageLabel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*


class DayAutoServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val oilTypeImageLabel: ImageLabel
    private val locationImageLabel: ImageLabel
    private val vehicleImageLabel: ImageLabel
    private val userImageLabel: ImageLabel
    private val statusImageView: ImageView
    private val timeTextView: TextView
    private val connectingViewFull: View
    private val connectingViewMid: View
    private val statusBackgroundView: View
    private val lottieAnimationView: View

    val context = view.context // Use itemView.context

    var isLastServiceOfDay: Boolean = false
        set(newValue) {
            field = newValue
            updateForLastServiceOfDay()
        }

    private fun updateForLastServiceOfDay() {
        if (isLastServiceOfDay) {
            connectingViewFull.visibility = View.GONE
            connectingViewMid.visibility = View.VISIBLE
        } else {
            connectingViewFull.visibility = View.VISIBLE
            connectingViewMid.visibility = View.GONE
        }
    }

    fun configure(
        autoServiceElements: AutoServiceListElements,
        listener: (AutoServiceListElements) -> Unit
    ) {
        val scheduledDate = autoServiceElements.autoService.scheduledDate

        oilTypeImageLabel.text = autoServiceElements.oilChange?.oilType?.localizedString() ?: ""
        locationImageLabel.text = autoServiceElements.location.streetAddress ?: ""
        val vName = autoServiceElements.vehicle.name ?: ""
        val vPlate = autoServiceElements.vehicle.licensePlate
        val vState = autoServiceElements.vehicle.state
        vehicleImageLabel.text = "$vName • $vPlate • $vState"
        userImageLabel.text = autoServiceElements.user?.displayName() ?: ""

        itemView.setOnClickListener {
            listener(autoServiceElements)
        }

        statusBackgroundView.backgroundTintList = statusColorStateList(autoServiceElements.autoService.status ?: AutoServiceStatus.scheduled)
        
        val status = autoServiceElements.autoService.status
        if (status == AutoServiceStatus.inProgress) {
            statusImageView.visibility = View.GONE
            lottieAnimationView.visibility = View.VISIBLE
        } else {
            statusImageView.visibility = View.VISIBLE
            lottieAnimationView.visibility = View.GONE
            val r = statusImage(status ?: AutoServiceStatus.scheduled)
            if (r != null) {
                statusImageView.setImageResource(r)
            }
        }
        
        if (scheduledDate != null) {
            val localDateTime = LocalDateTime.ofInstant(scheduledDate.toInstant(), scheduledDate.timeZone.toZoneId())
            timeTextView.text = timeFormatter.format(localDateTime)
        }
    }

    private fun statusColorStateList(status: AutoServiceStatus): ColorStateList? {
        val colorId = when (status) {
            AutoServiceStatus.scheduled -> R.color.statusColorScheduled
            AutoServiceStatus.canceled -> R.color.statusColorCanceled
            AutoServiceStatus.inProgress -> R.color.statusColorInProgress
            AutoServiceStatus.completed -> R.color.statusColorCompleted
        }
        return ContextCompat.getColorStateList(applicationContext, colorId)
    }

    private fun statusImage(status: AutoServiceStatus): Int? {
        return when (status) {
            AutoServiceStatus.scheduled -> R.drawable.ic_calendar_filled
            AutoServiceStatus.canceled -> R.drawable.ic_x
            AutoServiceStatus.inProgress -> null
            AutoServiceStatus.completed -> R.drawable.ic_checkmark
        }
    }

    init {
        val oil: ImageLabel = view.findViewById(R.id.oilTypeImageLabel)
        oil.imageType = ImageLabel.ImageType.OIL
        oilTypeImageLabel = oil

        val lImageLabel: ImageLabel = view.findViewById(R.id.addressImageLabel)
        lImageLabel.imageType = ImageLabel.ImageType.LOCATION
        locationImageLabel = lImageLabel

        val vLabel: ImageLabel = view.findViewById(R.id.vehicleImageLabel)
        vLabel.imageType = ImageLabel.ImageType.VEHICLE
        vehicleImageLabel = vLabel

        val uLabel: ImageLabel = view.findViewById(R.id.userImageLabel)
        uLabel.imageType = ImageLabel.ImageType.PERSON
        userImageLabel = uLabel

        statusImageView = view.findViewById(R.id.statusImageView)
        timeTextView = view.findViewById(R.id.timeTextView)
        connectingViewFull = view.findViewById(R.id.connectingViewFull)
        connectingViewMid = view.findViewById(R.id.connectingViewMid)
        statusBackgroundView = view.findViewById(R.id.statusBackgroundView)
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)

        updateForLastServiceOfDay()
    }

    companion object {
        val timeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendPattern("h:mm ")
            .appendText(ChronoField.AMPM_OF_DAY, mapOf(0L to "am", 1L to "pm"))
            .toFormatter(Locale.US)
    }
    
}