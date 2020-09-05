package com.carswaddle.carswaddleandroid.activities.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.Extensions.isEmpty
import com.carswaddle.carswaddleandroid.Extensions.isValidEmail
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.activity_login
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.UserRepository


class LoginActivity : AppCompatActivity() {

    private val passwordEditText: EditText by lazy { findViewById(R.id.password_edit_text) as EditText }
    private val emailEditText: EditText by lazy { findViewById(R.id.email_edit_text) as EditText }
    private val loginButton: Button by lazy { findViewById(R.id.loginButton) as Button }

    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        setContentView(activity_login)

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateLoginButton()
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateLoginButton()
            }
        })

        loginButton.setOnClickListener {
            didTapLogin()
        }

        updateLoginButton()
    }

    private fun isLoginButtonEnabled(): Boolean {
        return !passwordEditText.isEmpty() && emailEditText.text.toString().isValidEmail()
    }

    private fun updateLoginButton() {
        loginButton.isEnabled = isLoginButtonEnabled()
    }

    private fun didTapLogin() {
        val auth = Authentication(applicationContext)
        userRepo.login(emailEditText.text.toString(), passwordEditText.text.toString(), this) { throwable, authResponse ->
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
