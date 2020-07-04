package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.content.Context
import android.media.Image
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.activities.ui.home.AutoServiceListElements
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.location.LocationRepository
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicDao
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import kotlinx.android.synthetic.main.date_display.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

class AutoServiceListAdapter(private val autoServices: List<AutoServiceListElements>) :
    RecyclerView.Adapter<AutoServiceViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
//    class AutoServiceViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
//
//    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//        val textView: TextView
//
//        init {
//            // Define click listener for the ViewHolder's View.
//            v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
//            textView = v.findViewById(R.id.textView)
//        }
//    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoServiceViewHolder {
        // create a new view
        val autoservicesListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.autoservices_list_item, parent, false)

        return AutoServiceViewHolder(autoservicesListView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: AutoServiceViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textView.text = autoServices[position].notes
        holder.configure(autoServices[position])
    }


    override fun getItemCount() = autoServices.size
}

//class DateDisplayView(context: Context, attrs: AttributeSet) : ConstraintLayout {
//
//    constructor(context: Context) {
//
//    }
//
//    init() {
//
//    }
//
//}



class DateDisplayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val dayOfMonthTextView = findViewById<TextView>(R.id.dayOfMonth)
    val monthTextView = findViewById<TextView>(R.id.month)
    val dayOfWeekAndTimeTextView = findViewById<TextView>(R.id.dayAndTime)

    init {

    }

    fun configure(calendar: Calendar) {
        dayOfMonthTextView.text = SimpleDateFormat("dd").format(calendar.getTime())
        monthTextView.text = SimpleDateFormat("MMM").format(calendar.getTime())
        dayOfWeekAndTimeTextView.text = SimpleDateFormat("EEE hh:hh aa").format(calendar.getTime())
    }

}



class ImageLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val image: ImageView = findViewById<ImageView>(R.id.imageLabelImageView)
    private val textView: TextView = findViewById<TextView>(R.id.imageLabelLabel)

    enum class ImageType { VEHICLE, LOCATION, OIL, PERSON }

    var imageType: ImageType = ImageType.VEHICLE
        set(value) {
            field = value
            updateImage()
        }

    private fun updateImage() {
        image.setImageResource(imageForCurrentImageType())
    }

    private fun imageForCurrentImageType(): Int {
        return R.drawable.ic_car_swaddle_pin
        // TODO: get correct images
    }

    var text: String
        get() = textView.text as String
        set(value) { textView.text = value }

    init {

    }

}

class AutoServiceViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val mechanicImageLabel: ImageLabel by lazy {
        val label: ImageLabel = view.findViewById(R.id.mechanicImageLabel)
        label.imageType = ImageLabel.ImageType.VEHICLE
        label
    }

    private val locationImageLabel: ImageLabel by lazy {
        val label: ImageLabel = view.findViewById(R.id.addressImageLabel)
        label.imageType = ImageLabel.ImageType.LOCATION
        label
    }

    private val vehicleImageLabel: ImageLabel by lazy {
        val label: ImageLabel = view.findViewById(R.id.vehicleImageLabel)
        label.imageType = ImageLabel.ImageType.PERSON
        label
    }


    val dateDisplayView: DateDisplayView = view.findViewById(R.id.date_display)
    val context = view.context

    fun configure(autoServiceElements: AutoServiceListElements) {
        if (autoServiceElements.autoService.scheduledDate != null) {
            dateDisplayView.configure(autoServiceElements.autoService.scheduledDate)
        } else {
            // TODO: something went wrong
        }

        mechanicImageLabel.text = autoServiceElements.mechanicUser.firstName ?: ""
        locationImageLabel.text = autoServiceElements.location.streetAddress
        vehicleImageLabel.text = autoServiceElements.vehicle.name ?: ""
    }

    init {
        // Define click listener for the ViewHolder's View.
//        v.setOnClickListener { Log.d(TAG, "Element $adapterPosition clicked.") }
//        textView = v.findViewById(R.id.textView)

    }
}


