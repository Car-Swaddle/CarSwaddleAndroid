package com.carswaddle.carswaddleandroid.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import com.carswaddle.carswaddleandroid.Extensions.afterTextChanged
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.R.layout.activity_set_phone_number
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.user.UserRepository


class SetPhoneNumberActivity: Activity() {

    private val phoneNumberEditText: EditText by lazy { findViewById(R.id.phoneNumberEditText) as EditText }
    private val saveButton: Button by lazy { findViewById(R.id.savePhoneNumberButton) as Button }

    lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())

        setContentView(activity_set_phone_number)

        saveButton.setOnClickListener {
            savePhoneNumber()
        }

        phoneNumberEditText.afterTextChanged {
            updateButtonEnabledness()
        }

    }

    private fun updateButtonEnabledness() {
        saveButton.isEnabled = PhoneNumberUtils.isGlobalPhoneNumber(phoneNumberEditText.text.toString())
    }

    private fun savePhoneNumber() {
        val phone = phoneNumberEditText.text.toString()
        if (phone == null && PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            return
        }
        userRepo.updatePhoneNumber(phone, this, {

        }) {
            if (it == null) {
                val intent = Intent(this, VerifySMSCodeActivity::class.java)
                startActivity(intent)
            } else {

            }
        }
    }

}