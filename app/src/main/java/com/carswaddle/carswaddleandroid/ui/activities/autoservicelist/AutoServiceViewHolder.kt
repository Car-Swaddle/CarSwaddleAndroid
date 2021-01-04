package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.ImageLabel
import com.carswaddle.carswaddleandroid.R


class AutoServiceViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val mechanicImageLabel: ImageLabel
    private val locationImageLabel: ImageLabel
    private val vehicleImageLabel: ImageLabel

    val dateDisplayView: DateDisplayView = view.findViewById(R.id.date_display)
    val context = view.context // Use itemView.context

    fun configure(autoServiceElements: AutoServiceListElements, listener: (AutoServiceListElements) -> Unit) {
        val d = autoServiceElements.autoService.scheduledDate
        if (d != null) {
            dateDisplayView.configure(d)
        } else {
            // TODO: something went wrong
        }

        mechanicImageLabel.text = autoServiceElements.mechanicUser.firstName ?: ""
        locationImageLabel.text = autoServiceElements.location.streetAddress ?: ""
        vehicleImageLabel.text = autoServiceElements.vehicle.name ?: ""
        
        itemView.setOnClickListener {
            listener(autoServiceElements)
        }
    }

    init {
        val mImageLabel: ImageLabel = view.findViewById(R.id.mechanicImageLabel)
        mImageLabel.imageType = ImageLabel.ImageType.PERSON
        mechanicImageLabel = mImageLabel

        val lImageLabel: ImageLabel = view.findViewById(R.id.addressImageLabel)
        lImageLabel.imageType = ImageLabel.ImageType.LOCATION
        locationImageLabel = lImageLabel

        val vLabel: ImageLabel = view.findViewById(R.id.vehicleImageLabel)
        vLabel.imageType = ImageLabel.ImageType.VEHICLE
        vehicleImageLabel = vLabel
    }
}
