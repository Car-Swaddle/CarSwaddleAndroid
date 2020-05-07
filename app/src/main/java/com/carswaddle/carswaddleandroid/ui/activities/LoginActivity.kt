package com.carswaddle.carswaddleandroid.activities.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.Extensions.isEmpty
import com.carswaddle.carswaddleandroid.Extensions.isValidEmail
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.login
import com.carswaddle.carswaddleandroid.data.Authentication


class LoginActivity : AppCompatActivity() {

    private val passwordEditText: EditText by lazy { findViewById(R.id.password_edit_text) as EditText }
    private val emailEditText: EditText by lazy { findViewById(R.id.email_edit_text) as EditText }
    private val signUpButton: Button by lazy { findViewById(R.id.sign_up_button) as Button }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(login)

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateSignUpButton()
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateSignUpButton()
            }
        })

        signUpButton.setOnClickListener {
            didTapSignUp()
        }

        updateSignUpButton()
    }

    private fun isSignUpButtonEnabled(): Boolean {
        return !passwordEditText.isEmpty() && emailEditText.text.toString().isValidEmail()
    }

    private fun updateSignUpButton() {
        signUpButton.isEnabled = isSignUpButtonEnabled()
    }

    private fun didTapSignUp() {
        val auth = Authentication(applicationContext)
        auth.login(emailEditText.text.toString(), passwordEditText.text.toString()) { throwable, authResponse ->
            if (throwable == null && auth.isUserLoggedIn()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.d("dunno", "Unable to login")
            }
        }
    }

}
