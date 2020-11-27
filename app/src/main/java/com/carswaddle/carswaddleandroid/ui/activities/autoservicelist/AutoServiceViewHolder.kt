package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails.AutoServiceDetailsFragment
import java.util.*

//interface AutoServiceViewHolderDelegate {
//    fun didSelectItem(autoServiceElements: AutoServiceListElements)
//}


class AutoServiceViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val mechanicImageLabel: ImageLabel
    private val locationImageLabel: ImageLabel
    private val vehicleImageLabel: ImageLabel

    val dateDisplayView: DateDisplayView = view.findViewById(R.id.date_display)
    val context = view.context // Use itemView.context

    fun configure(autoServiceElements: AutoServiceListElements, listener: (AutoServiceListElements) -> Unit) {
        if (autoServiceElements.autoService.scheduledDate != null) {
            dateDisplayView.configure(autoServiceElements.autoService.scheduledDate)
        } else {
            // TODO: something went wrong
        }

        mechanicImageLabel.text = autoServiceElements.mechanicUser.firstName ?: ""
        locationImageLabel.text = autoServiceElements.location.streetAddress ?: ""
        vehicleImageLabel.text = autoServiceElements.vehicle.name ?: ""

        itemView.context

        // Show the fragment from this class

        itemView.setOnClickListener {
            listener(autoServiceElements)
        }
    }

    init {
        val mImageLabel: ImageLabel = view.findViewById(R.id.mechanicImageLabel)
        mImageLabel.imageType = ImageLabel.ImageType.VEHICLE
        mechanicImageLabel = mImageLabel

        val lImageLabel: ImageLabel = view.findViewById(R.id.addressImageLabel)
        lImageLabel.imageType = ImageLabel.ImageType.LOCATION
        locationImageLabel = lImageLabel

        val vLabel: ImageLabel = view.findViewById(R.id.vehicleImageLabel)
        vLabel.imageType = ImageLabel.ImageType.PERSON
        vehicleImageLabel = vLabel
    }
}
