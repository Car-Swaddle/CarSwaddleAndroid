package com.carswaddle.carswaddlemechanic.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.carswaddle.carswaddlemechanic.R


class ImageLabel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val image: ImageView
    private val textView: TextView

    enum class ImageType { VEHICLE, LOCATION, OIL, PERSON, TIME }

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
            ImageType.TIME ->
                return R.drawable.ic_basic_clock
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

}