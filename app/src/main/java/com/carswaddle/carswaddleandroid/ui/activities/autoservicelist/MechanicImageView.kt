package com.carswaddle.carswaddleandroid.ui.activities.autoservicelist

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.target.ViewTarget
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.Authentication

private const val mechanicProfileImage = "/api/data/mechanic/profile-picture/{mechanicId}"

class MechanicImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private val imageView: ImageView
    private var auth = Authentication(context)

    var mechanicId: String? = null 
    set(newValue) {
        updateImage()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.mechanic_image_view, this, true)
        imageView = findViewById<ImageView>(R.id.imageView)
    }
    
    private fun updateImage() {

        Glide.with(context).pauseAllRequests()
        
        val url = glidUrl()
        if (url == null) {
            return
        }
         Glide.with(context)
            .load(url)
            .centerCrop()
            .into(imageView)
    }
    
    private fun glidUrl(): GlideUrl? {
        val id = mechanicId
        val token = auth.getAuthToken()
        if (id == null || token == null) {
            return null
        }

        var url = mechanicProfileImage
        url.replace("{mechanicId}", id)

        return GlideUrl(url,
            LazyHeaders.Builder()
            .addHeader("Authorization", "Bearer " + token)
            .build()
        )
    }

}