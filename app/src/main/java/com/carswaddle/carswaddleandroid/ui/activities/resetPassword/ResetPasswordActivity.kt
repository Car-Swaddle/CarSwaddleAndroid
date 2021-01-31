package com.carswaddle.carswaddleandroid.ui.activities.resetPassword

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.R.layout.activity_reset_password
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.store.AppDatabase

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var userRepo: UserRepository


    private val newPasswordEditText: EditText by lazy { findViewById(R.id.newPasswordEditText) as EditText }
    private val resetButton: ProgressButton by lazy { findViewById(R.id.resetPasswordButton) as ProgressButton }

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

        resetButton.button.setOnClickListener {
            didTapReset()
        }

        newPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateResetButton()
            }
        })

        updateResetButton()
    }

    private fun updateResetButton() {
        resetButton.isButtonEnabled = isValidPassword()
    }

    private fun didTapReset() {
        val newPassword = newPasswordEditText.text.toString()
        val token = resetToken
        if (token == null) {
            return
        }
        if (isValidPassword() == false) {
            return
        }
        resetButton.isLoading = true

        userRepo.resetPassword(newPassword, token) {
            runOnUiThread { 
                resetButton.isLoading = false
                if (it == null) {
                    Log.d("ui", "successful reset")
                    showSuccessDialog()
                }
            }
        }
    }

    private fun isValidPassword(): Boolean {
        val p = newPasswordEditText.text.toString()
        return p != null && p.isNullOrBlank() == false && p.count() >= 3
    }

    private fun showSuccessDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle(R.string.success_reset_password_title)
            .setMessage(R.string.success_reset_password_message)
            .setCancelable(true)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })

        val alert = dialogBuilder.create()
        alert.show()
    }


}
