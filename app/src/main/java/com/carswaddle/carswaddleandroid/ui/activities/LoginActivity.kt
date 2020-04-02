package com.carswaddle.carswaddleandroid.activities.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R

class LoginActivity : AppCompatActivity() {

    private val passwordTextView: TextView = findViewById(R.id.password_text_view)
    private val emailTextView: TextView = findViewById(R.id.email_text_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login)


        passwordTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })



    }

}