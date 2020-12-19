package com.carswaddle.carswaddleandroid.ui.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.R.layout.activity_forgot_password
import com.carswaddle.carswaddleandroid.data.user.EmailNotFoundError
import com.carswaddle.store.AppDatabase

class ForgotPasswordActivity: AppCompatActivity() {

    private val explainTextView: TextView by lazy { findViewById(R.id.explainForgotEditText) as TextView }
    private val emailEditText: EditText by lazy { findViewById(R.id.emailEditText) as EditText }
    private val sendButton: Button by lazy { findViewById(R.id.sendResetButton) as Button }

    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_forgot_password)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        sendButton.setOnClickListener {
            sendResetLink()
        }
    }

    private fun sendResetLink() {
        val email = emailEditText.text.toString()
        if (email == null) {
            return
        }
        userRepo.sendResetLink(email,this) {
            if (it == null) {
                showSuccessDialog(email)
            } else if (it is EmailNotFoundError) {
                showErrorDialog(getString(R.string.error_sending_reset_link_title), getString(R.string.error_sending_reset_link_message))
            } else {
                showErrorDialog(getString(R.string.error_sending_reset_link_title), getString(R.string.error_sending_reset_link_message))
            }
        }
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

}