package com.carswaddle.carswaddleandroid.ui.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.activity_verify_code
import com.carswaddle.carswaddleandroid.activities.ui.MainActivity
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.Authentication
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.mukesh.OtpView


class VerifySMSCodeActivity: AppCompatActivity() {

    private val verify: OtpView by lazy { findViewById(R.id.codeEntryView) as OtpView }
    private val verifyButton: Button by lazy { findViewById(R.id.verifyCodeButton) as Button }
    private val resendButton: Button by lazy { findViewById(R.id.resendCodeButton) as Button }
    private val verifyTextView: TextView by lazy { findViewById(R.id.verifyCodeEditText) as TextView }
    private val sentCodeTextView: TextView by lazy { findViewById(R.id.sentCodeTextView) as TextView }

    private lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_verify_code)

        val db = AppDatabase.getDatabase(this)
        userRepo = UserRepository(db.userDao())

        verify.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.w("android", "changed text verify")
                if (verify.text?.count() == 5) {
                    verifySMSCode()
                }
            }
        })

        verify.setOtpCompletionListener {

        }

        resendButton.setOnClickListener {
            sendVerificationCode()
        }

        verifyButton.setOnClickListener {
            verifySMSCode()
        }

        verify.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    val imm: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                    imm?.showSoftInput(verify, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        })

        val user = userRepo.getCurrentUser(this)
        sentCodeTextView.text = getString(R.string.sent_code, user?.phoneNumber ?: "")

        sendVerificationCode()

        verify.requestFocus()
    }

    private fun sendVerificationCode() {
        userRepo.sendSMSVerificationSMS(this) {
            if (it != null) {
                Log.w("android", "unable to send verification")
            }
        }
    }

    private fun verifySMSCode() {
        val code = verify.text.toString()
        if (code.isNullOrBlank()) {
            return
        }
        userRepo.verifySMSCode(this, code) {
            if (it == null) {
                Log.w("Android", "verified code")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.w("android", "Didn't work verify code")
                showErrorAlert()
            }
        }
    }

    private fun showErrorAlert() {
        val dialogBuilder = AlertDialog.Builder(this)

        val user = userRepo.getCurrentUser(this)

        val s = getString(R.string.error_verifying, user?.phoneNumber ?: "")
        dialogBuilder.setMessage(s)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                verify.setText("")
            })
            .setNeutralButton(R.string.update_phone, DialogInterface.OnClickListener { dialog, id ->
                val intent = Intent(this, SetPhoneNumberActivity::class.java)
                startActivity(intent)
            })

        val alert = dialogBuilder.create()
        alert.setTitle(R.string.error_verifying_title)
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profile_action_logout) {
            Log.w("car swaddle android", "logged out∂")
            val db = AppDatabase.getDatabase(applicationContext)
            db?.clearAllTables()

            Authentication(applicationContext).logout { error, response ->
                val intent = Intent(this, PreAuthenticationActivity::class.java)

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

}