package com.carswaddle.carswaddlemechanic.ui.calendar

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.ui.activities.autoservicelist.AutoServiceListElements
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.common.ImageLabel


class DayAutoServiceViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val mechanicImageLabel: ImageLabel
    private val locationImageLabel: ImageLabel
    private val vehicleImageLabel: ImageLabel
    private val userImageLabel: ImageLabel

    val context = view.context // Use itemView.context

    fun configure(autoServiceElements: AutoServiceListElements, listener: (AutoServiceListElements) -> Unit) {
        val d = autoServiceElements.autoService.scheduledDate

        mechanicImageLabel.text = autoServiceElements.mechanicUser.firstName ?: ""
        locationImageLabel.text = autoServiceElements.location.streetAddress ?: ""
        vehicleImageLabel.text = autoServiceElements.vehicle.name ?: ""
        userImageLabel.text = autoServiceElements.user?.displayName() ?: "" 

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

        val uLabel: ImageLabel = view.findViewById(R.id.userImageLabel)
        uLabel.imageType = ImageLabel.ImageType.PERSON
        userImageLabel = uLabel
    }
}