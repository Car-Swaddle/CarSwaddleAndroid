package com.carswaddle.carswaddleandroid.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.activity_pre_auth

class PreAuthenticationActivity : AppCompatActivity() {
    
//    private var ambienceView: AmbienceView by lazy { return findViewById(R.id.ambience_view) as AmbienceView }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT < 16) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }
        
        setContentView(activity_pre_auth)
        
        val ambienceView: AmbienceView = findViewById(R.id.ambience_view)
        ambienceView.startAnimation()

        supportActionBar?.hide()

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()
    }
    
}