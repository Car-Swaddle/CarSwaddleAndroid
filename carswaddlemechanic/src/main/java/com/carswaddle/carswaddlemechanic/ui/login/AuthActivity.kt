package com.carswaddle.carswaddlemechanic.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.R.layout.activity_auth

class AuthActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT < 16) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }

        setContentView(activity_auth)

        val ambienceView: AmbienceView = findViewById(R.id.ambience_view)
        ambienceView.startAnimation()

        supportActionBar?.hide()

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        actionBar?.hide()
    }

}
