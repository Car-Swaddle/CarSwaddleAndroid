package com.carswaddle.carswaddlemechanic.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.carswaddle.carswaddleandroid.Extensions.dismissKeyboard
import com.carswaddle.carswaddleandroid.Extensions.isEmpty
import com.carswaddle.carswaddleandroid.Extensions.isValidEmail
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.MainActivity
import com.carswaddle.carswaddlemechanic.R.layout.fragment_login
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase


class LoginFragment : Fragment() {

    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loginButton: ProgressButton
    private lateinit var forgotPasswordButton: ProgressButton
    private lateinit var statusTextView: TextView

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
        statusTextView = root.findViewById(R.id.status_text_view)

        statusTextView.visibility = View.GONE

        val e = arguments?.getString("preExistingEmail")
        if (e != null) {
            emailEditText.setText(e)
        }

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateLoginButton()
                statusTextView.visibility = View.GONE
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateLoginButton()
                statusTextView.visibility = View.GONE
            }
        })

        root.setOnClickListener {
            dismissKeyboard()
        }

        loginButton.button.setOnClickListener {
            didTapLogin()
        }

        forgotPasswordButton.button.setOnClickListener {
            didTapForgotPassword()
        }

        updateLoginButton()

        return root
    }

    private fun didTapForgotPassword() {
//        val intent = Intent(requireActivity(), ForgotPasswordActivity::class.java)
//        intent.putExtra(ForgotPasswordActivity.EMAIL, emailEditText.text.toString())
//        startActivity(intent)
    }

    private fun isLoginButtonEnabled(): Boolean {
        return !passwordEditText.isEmpty() && emailEditText.text.toString().isValidEmail()
    }

    private fun updateLoginButton() {
        loginButton.isButtonEnabled = isLoginButtonEnabled()
    }

    private fun didTapLogin() {
        loginButton.isLoading = true

        statusTextView.visibility = View.GONE

        val auth = Authentication(requireContext())
        userRepo.login(emailEditText.text.toString(), passwordEditText.text.toString(), true, requireContext()) { throwable, authResponse ->
            requireActivity().runOnUiThread { loginButton.isLoading = false }
            if (throwable == null && auth.isUserLoggedIn()) {
//                val user = userRepo.getCurrentUser(applicationContext)
//                if (user == null) {
//                    Log.d("dunno", "something messed up, no user, but signed in")
//                } else if (user.firstName.isNullOrBlank() || user.lastName.isNullOrBlank()) {
//                    val intent = Intent(requireActivity(), SetNameActivity::class.java)
//                    startActivity(intent)
//                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == null || user.isPhoneNumberVerified == false) {
//                    val intent = Intent(requireActivity(), SetPhoneNumberActivity::class.java)
//                    startActivity(intent)
//                } else {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
//                }
            } else {
                Log.d("dunno", "Unable to login")
                requireActivity().runOnUiThread {
                    val text = getString(R.string.login_failure)
                    statusTextView.visibility = View.VISIBLE
                    statusTextView.setText(text)
                }
            }
        }
    }

}
