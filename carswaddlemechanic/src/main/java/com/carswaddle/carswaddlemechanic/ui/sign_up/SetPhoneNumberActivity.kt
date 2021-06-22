package com.carswaddle.carswaddlemechanic.ui.sign_up

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.Extensions.afterTextChanged
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.ui.login.AuthActivity
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase


class SetPhoneNumberActivity: AppCompatActivity() {

    private val phoneNumberEditText: EditText by lazy { findViewById(R.id.phoneNumberEditText) as EditText }
    private val saveButton: ProgressButton by lazy { findViewById(R.id.savePhoneNumberButton) as ProgressButton }

    lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())
        
        setContentView(R.layout.activity_set_phone_number)

        saveButton.button.setOnClickListener {
            savePhoneNumber()
        }

        phoneNumberEditText.afterTextChanged {
            updateButtonEnabledness()
        }
    }

    private fun updateButtonEnabledness() {
        saveButton.isButtonEnabled = PhoneNumberUtils.isGlobalPhoneNumber(phoneNumberEditText.text.toString())
    }

    private fun savePhoneNumber() {
        val phone = phoneNumberEditText.text.toString()
        if (phone == null && PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            return
        }

        saveButton.isLoading = true
        
        userRepo.updatePhoneNumber(phone, this, {

        }) {
            runOnUiThread { saveButton.isLoading = false }
            if (it == null) {
                val intent = Intent(this, VerifySMSCodeActivity::class.java)
                startActivity(intent)
            } else {

            }
        }
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
                val intent = Intent(this, AuthActivity::class.java)

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

}