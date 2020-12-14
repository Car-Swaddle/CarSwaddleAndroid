package com.carswaddle.carswaddleandroid.activities.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.carswaddle.carswaddleandroid.CarSwaddleApp.CarSwaddleApp.Companion.applicationContext
import com.carswaddle.carswaddleandroid.Extensions.isEmpty
import com.carswaddle.carswaddleandroid.Extensions.isValidEmail
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.fragment_login
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.ui.activities.ForgotPasswordActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetNameActivity
import com.carswaddle.carswaddleandroid.ui.activities.SetPhoneNumberActivity


class LoginFragment : Fragment() {

    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: Button

    private lateinit var userRepo: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(fragment_login, container, false)
        
        val db = AppDatabase.getDatabase(requireContext())
        userRepo = UserRepository(db.userDao())

        passwordEditText = root.findViewById(R.id.password_edit_text)
        emailEditText = root.findViewById(R.id.emailEditText)
        loginButton = root.findViewById(R.id.sendResetButton)
        forgotPasswordButton = root.findViewById(R.id.forgotPasswordButton)
        
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

        forgotPasswordButton.setOnClickListener {
            didTapForgotPassword()
        }

        updateLoginButton()
        
        return root
    }

    private fun didTapForgotPassword() {
        val intent = Intent(requireActivity(), ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun isLoginButtonEnabled(): Boolean {
        return !passwordEditText.isEmpty() && emailEditText.text.toString().isValidEmail()
    }

    private fun updateLoginButton() {
        loginButton.isEnabled = isLoginButtonEnabled()
    }

    private fun didTapLogin() {
        val auth = Authentication(requireContext())
        userRepo.login(emailEditText.text.toString(), passwordEditText.text.toString(), requireContext()) { throwable, authResponse ->
            if (throwable == null && auth.isUserLoggedIn()) {
                val user = userRepo.getCurrentUser(applicationContext)
                if (user == null) {
                    Log.d("dunno", "something messed up, no user, but signed in")
                } else if (user.firstName.isNullOrBlank() || user.lastName.isNullOrBlank()) {
//                    val intent = Intent(this, SetNameActivity::class.java)
//                    startActivity(intent)
                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == null || user.isPhoneNumberVerified == false) {
//                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
//                    startActivity(intent)
                } else {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } else {
                Log.d("dunno", "Unable to login")
            }
        }
    }

}
