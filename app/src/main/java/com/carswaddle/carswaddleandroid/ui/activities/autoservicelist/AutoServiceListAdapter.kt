package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.home.AutoServiceListElements
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class AutoServiceListAdapter(private val autoServices: LiveData<List<AutoServiceListElements>>) :
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
        val autoService = autoServices.value?.get(position)
        if (autoService != null) {
            holder.configure(autoService)
        }
    }


    override fun getItemCount(): Int {
        return autoServices.value?.count() ?: 0
    }
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
) : LinearLayout(context, attrs, defStyleAttr) {

    val dayOfMonthTextView: TextView
    val monthTextView: TextView
    val dayOfWeekAndTimeTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.date_display, this, true)

        dayOfMonthTextView = findViewById<TextView>(R.id.dayOfMonth)
        monthTextView = findViewById<TextView>(R.id.month)
        dayOfWeekAndTimeTextView = findViewById<TextView>(R.id.dayAndTime)
    }

    fun configure(calendar: Calendar) {
        dayOfMonthTextView.text = SimpleDateFormat("dd").format(calendar.getTime())
        monthTextView.text = SimpleDateFormat("MMM").format(calendar.getTime())
        dayOfWeekAndTimeTextView.text = dayOfWeekAndTime(calendar)
    }

    private fun dayOfWeekAndTime(calendar: Calendar): String {
        val symbols = DateFormatSymbols(Locale.getDefault())
        symbols.setAmPmStrings(arrayOf("am", "pm"))
        return SimpleDateFormat("EEE hh:mm aa", symbols).format(calendar.getTime())
    }

}



//class ImageLabel : View {
class ImageLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val image: ImageView
    private val textView: TextView

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
        when (imageType) {
            ImageType.VEHICLE ->
                return R.drawable.car
            ImageType.LOCATION ->
                return R.drawable.pin
            ImageType.PERSON ->
                return R.drawable.ic_user_male
            ImageType.OIL ->
                return R.drawable.engine_oil
        }
    }

    var text: String
        get() = textView.text as String
        set(value) { textView.text = value }

    init {
        LayoutInflater.from(context).inflate(R.layout.image_label, this, true)
        orientation = HORIZONTAL

        image = findViewById<ImageView>(R.id.image_label_imageView)
        textView = findViewById<TextView>(R.id.image_label_label)
    }

//    init {
////        View.inflate(context, R.layout.image_label, null)
//        View.inflate(context, R.layout.image_label, this)
//
//        image = findViewById<ImageView>(R.id.image_label_imageView)
//        textView = findViewById<TextView>(R.id.image_label_label)
//        Log.d("tag", "init image label")
//    }

}

class AutoServiceViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val mechanicImageLabel: ImageLabel
    private val locationImageLabel: ImageLabel
    private val vehicleImageLabel: ImageLabel


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


