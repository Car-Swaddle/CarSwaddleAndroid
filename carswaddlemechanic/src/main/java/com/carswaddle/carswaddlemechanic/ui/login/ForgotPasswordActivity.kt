package com.carswaddle.carswaddlemechanic.ui.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.Extensions.isEmpty
import com.carswaddle.carswaddleandroid.Extensions.isValidEmail
import com.carswaddle.carswaddleandroid.data.user.EmailNotFoundError
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.store.AppDatabase

class ForgotPasswordActivity: AppCompatActivity() {

    private val explainTextView: TextView by lazy { findViewById(R.id.explainForgotEditText) as TextView }
    private val emailEditText: EditText by lazy { findViewById(R.id.emailEditText) as EditText }
    private val sendButton: ProgressButton by lazy { findViewById(R.id.sendResetButton) as ProgressButton }

    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot_password)
        
        val email = intent.getStringExtra(EMAIL) ?: ""
        emailEditText.setText(email)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        sendButton.button.setOnClickListener {
            sendResetLink()
        }

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateSendButton()
            }
        })
        
        updateSendButton()
    }

    private fun sendResetLink() {
        if (isSendButtonEnabled() == false) {
            return
        }
        val email = emailEditText.text.toString()
        sendButton.isLoading = true
        userRepo.sendResetLink(email,this) {
            runOnUiThread { sendButton.isLoading = false }
            if (it == null) {
                showSuccessDialog(email)
            } else if (it is EmailNotFoundError) {
                showErrorDialog(getString(R.string.error_sending_reset_link_title), getString(R.string.error_sending_reset_link_message))
            } else {
                showErrorDialog(getString(R.string.error_sending_reset_link_title), getString(R.string.error_sending_reset_link_message))
            }
        }
    }

    private fun isSendButtonEnabled(): Boolean {
        return !emailEditText.isEmpty() && emailEditText.text.toString().isValidEmail()
    }

    private fun updateSendButton() {
        sendButton.isButtonEnabled = isSendButtonEnabled()
    }

    private fun showErrorDialog(title: String, message: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle(title).setMessage(message)
            .setCancelable(true)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->

            })

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun showSuccessDialog(email: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        val t = getString(R.string.success_sending_reset_link_title, email)
        dialogBuilder.setTitle(t).setMessage(getString(R.string.success_sending_reset_link_message))
            .setCancelable(true)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })

        val alert = dialogBuilder.create()
        alert.show()
    }

    companion object {
        val EMAIL = "EMAIL"
    }
    
}