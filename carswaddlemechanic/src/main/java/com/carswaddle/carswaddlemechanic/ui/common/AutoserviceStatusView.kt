package com.carswaddle.carswaddlemechanic.ui.common

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.carswaddle.carswaddleandroid.ImageLabel
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.application.CarSwaddleMechanicApp

class AutoserviceStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {
    
    var autoServiceStatus: AutoServiceStatus = AutoServiceStatus.scheduled
    set(newValue) {
        field = newValue
        updateforCurrentStatus()
    }
    
    private fun updateforCurrentStatus() {
        lottieView.visibility = lottieVisibilityForCurrentStatus()
        imageView.visibility = imageViewVisibilityForCurrentStatus()
        val imageId = statusImageForCurrentStatus()
        if (imageId != null) {
            imageView.setImageResource(imageId)
        }
        backgroundView.backgroundTintList = statusColorStateList(autoServiceStatus)
    }
    
    private fun imageViewVisibilityForCurrentStatus(): Int {
        return when(autoServiceStatus) {
            AutoServiceStatus.scheduled -> View.VISIBLE
            AutoServiceStatus.inProgress -> View.GONE
            AutoServiceStatus.completed -> View.VISIBLE
            AutoServiceStatus.canceled -> View.VISIBLE
        }
    }

    private fun statusImageForCurrentStatus(): Int? {
        return when (autoServiceStatus) {
            AutoServiceStatus.scheduled -> R.drawable.ic_calendar_filled
            AutoServiceStatus.canceled -> R.drawable.ic_x
            AutoServiceStatus.inProgress -> null
            AutoServiceStatus.completed -> R.drawable.ic_checkmark
        }
    }

    private fun lottieVisibilityForCurrentStatus(): Int {
        return when(autoServiceStatus) {
            AutoServiceStatus.scheduled -> View.GONE
            AutoServiceStatus.inProgress -> View.VISIBLE
            AutoServiceStatus.completed -> View.GONE
            AutoServiceStatus.canceled -> View.GONE
        }
    }

    private fun statusColorStateList(status: AutoServiceStatus): ColorStateList? {
        val colorId = when (status) {
            AutoServiceStatus.scheduled -> R.color.statusColorScheduled
            AutoServiceStatus.canceled -> R.color.statusColorCanceled
            AutoServiceStatus.inProgress -> R.color.statusColorInProgress
            AutoServiceStatus.completed -> R.color.statusColorCompleted
        }
        return ContextCompat.getColorStateList(CarSwaddleMechanicApp.applicationContext, colorId)
    }
    
    private var imageView: ImageView
    private var lottieView: LottieAnimationView
    private var backgroundView: View
    
    init {
        LayoutInflater.from(context).inflate(R.layout.view_autoservice_status, this, true)
        
        imageView = findViewById(R.id.statusImageView)
        lottieView = findViewById(R.id.lottieAnimationView)
        backgroundView = findViewById(R.id.backgroundView)
        
        
    }
    
}