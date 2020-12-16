package com.carswaddle.carswaddleandroid.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.carswaddle.carswaddleandroid.Extensions.*
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.MainActivity
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.UserRepository


private val carSwaddleTermsOfUse = "https://carswaddle.net/terms-of-use/"
private val carSwaddlePrivacyPolicy = "https://carswaddle.net/privacy-policy/"
private val stripeConnectAgreement = "https://stripe.com/connect-account/legal"


class SignUpFragment: Fragment() {

    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var agreementTextView: TextView

    private lateinit var goToLoginButton: Button

    private lateinit var userRepo: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sign_up, container, false)

        passwordEditText = root.findViewById(R.id.password_edit_text)
        emailEditText = root.findViewById(R.id.emailEditText)
        signUpButton = root.findViewById(R.id.sign_up_button)
        goToLoginButton = root.findViewById(R.id.go_to_login_button)
        agreementTextView = root.findViewById(R.id.agreement_text_view)
        
        val db = AppDatabase.getDatabase(requireContext())
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
            dismissKeyboard()
            didTapSignUp()
        }
        
        goToLoginButton.setOnClickListener {
            dismissKeyboard()
            val navController = requireActivity().findNavController(R.id.auth_nav_host)
            navController.navigate(R.id.action_signUpFragment_to_navigation_login, null)
        }

        updateSignUpButton()

        val linkColor = ContextCompat.getColor(requireContext(), R.color.brandContrast)
        
        agreementTextView.addLinks(
            Link("Car Swaddle Terms of Use Agreement", View.OnClickListener {
                val intent = Intent(requireActivity(), WebActivity::class.java)
                intent.putExtra(WebActivity.WEB_URL_KEY, carSwaddleTermsOfUse)
                intent.putExtra(
                    WebActivity.ACTIVITY_TITLE,
                    getString(R.string.car_swaddle_terms_of_use)
                )
                startActivity(intent)
            }, linkColor, true),
            Link("Car Swaddle Privacy Policy", View.OnClickListener {
                val intent = Intent(requireActivity(), WebActivity::class.java)
                intent.putExtra(WebActivity.WEB_URL_KEY, carSwaddlePrivacyPolicy)
                intent.putExtra(WebActivity.ACTIVITY_TITLE, getString(R.string.privacy_policy))
                startActivity(intent)
            }, linkColor, true),
            Link("Stripe Connected Account Agreement", View.OnClickListener {
                val intent = Intent(requireActivity(), WebActivity::class.java)
                intent.putExtra(WebActivity.WEB_URL_KEY, stripeConnectAgreement)
                intent.putExtra(WebActivity.ACTIVITY_TITLE, getString(R.string.stripe_agreement))
                startActivity(intent)
            }, linkColor, true)
        )
        
        return root
    }
    
    private fun isSignUpButtonEnabled(): Boolean {
        return !passwordEditText.isEmpty() && emailEditText.text.toString().isValidEmail()
    }

    private fun updateSignUpButton() {
        signUpButton.isEnabled = isSignUpButtonEnabled()
    }

    private fun didTapSignUp() {
        val auth = Authentication(requireContext())
        userRepo.signUp(
            emailEditText.text.toString(),
            passwordEditText.text.toString(),
            requireContext()
        ) { throwable, authResponse ->
            if (throwable == null && auth.isUserLoggedIn()) {
                val user = userRepo.getCurrentUser(requireContext())
                if (user == null) {
                    Log.d("dunno", "something messed up, no user, but signed in")
                } else if (user.firstName.isNullOrBlank() || user.lastName.isNullOrBlank()) {
                    val intent = Intent(requireActivity(), SetNameActivity::class.java)
                    startActivity(intent)
                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == false || user.isPhoneNumberVerified == false) {
                    val intent = Intent(requireActivity(), SetPhoneNumberActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            } else {
                Log.d("dunno", "Unable to sign up")
            }
        }
    }

}