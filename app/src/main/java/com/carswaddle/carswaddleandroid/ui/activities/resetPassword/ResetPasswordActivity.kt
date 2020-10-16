package com.carswaddle.carswaddleandroid.ui.activities.resetPassword

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.R.layout.activity_reset_password

class ResetPasswordActivity: AppCompatActivity() {

    private lateinit var userRepo: UserRepository


    private val newPasswordEditText: EditText by lazy { findViewById(R.id.newPasswordEditText) as EditText }
    private val resetButton: Button by lazy { findViewById(R.id.resetPasswordButton) as Button }

    private var resetToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_reset_password)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        val resetToken = intent?.data?.getQueryParameter("resetToken")
        if (resetToken != null) {
            this.resetToken = resetToken
        }

        resetButton.setOnClickListener {
            didTapReset()
        }

    }


    private fun didTapReset() {
        val newPassword = newPasswordEditText.text.toString()
        val token = resetToken
        if (newPassword == null || newPassword.isNullOrBlank() || newPassword.count() < 3 || token == null) {
            return
        }

        userRepo.resetPassword(newPassword, token) {
            if (it == null) {
                Log.d("ui", "successful reset")
                showSuccessDialog()
            }
        }
    }

    private fun showSuccessDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle(R.string.success_reset_password_title).setMessage(R.string.success_reset_password_message)
            .setCancelable(true)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })

        val alert = dialogBuilder.create()
        alert.show()
    }


}
