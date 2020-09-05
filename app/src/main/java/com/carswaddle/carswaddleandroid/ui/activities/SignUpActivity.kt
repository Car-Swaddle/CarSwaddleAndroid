package com.carswaddle.carswaddleandroid.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.carswaddle.carswaddleandroid.Extensions.isEmpty
import com.carswaddle.carswaddleandroid.Extensions.isValidEmail
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.activity_sign_up
import com.carswaddle.carswaddleandroid.activities.ui.MainActivity
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.UserRepository

class SignUpActivity: Activity() {

    private val passwordEditText: EditText by lazy { findViewById(R.id.password_edit_text) as EditText }
    private val emailEditText: EditText by lazy { findViewById(R.id.email_edit_text) as EditText }
    private val signUpButton: Button by lazy { findViewById(R.id.sign_up_button) as Button }

    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_sign_up)

        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())

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
        auth.signUp(emailEditText.text.toString(), passwordEditText.text.toString()) { throwable, authResponse ->
            if (throwable == null && auth.isUserLoggedIn()) {
                val user = userRepo.getCurrentUser(this)
                if (user == null) {
                    Log.d("dunno", "something messed up, no user, but signed in")
                } else if (user.displayName().isNullOrBlank()) {
                    val intent = Intent(this, SetNameActivity::class.java)
                    startActivity(intent)
                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == false) {
                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Log.d("dunno", "Unable to sign up")
            }
        }
    }

}