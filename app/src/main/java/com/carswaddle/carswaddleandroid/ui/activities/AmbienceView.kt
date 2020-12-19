package com.carswaddle.carswaddleandroid.ui.activities

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import com.carswaddle.carswaddleandroid.R


class AmbienceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    val imageView: ImageView
            
    init {
        LayoutInflater.from(context).inflate(R.layout.view_ambience, this, true)
        imageView = findViewById(R.id.ambience_image_view)
    }
    
    fun startAnimation() {
        val imageHeight = 2736f
        val imageWidth = 1539f

        val displayMetrics = context.resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        
        val a = TranslateAnimation(0f, -((imageWidth)+dpWidth), -(imageHeight+dpHeight), 0f)
        a.duration = 100000
        a.setRepeatCount(100)
        a.setRepeatMode(Animation.REVERSE)
        
        imageView.startAnimation(a)
    }
    
}