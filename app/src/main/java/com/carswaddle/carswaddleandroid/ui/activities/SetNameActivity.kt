package com.carswaddle.carswaddleandroid.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.carswaddle.carswaddleandroid.Extensions.afterTextChanged
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.activities.ui.MainActivity
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.user.UserRepository


class SetNameActivity: Activity() {

    private val firstNameEditText: EditText by lazy { findViewById(R.id.firstNameEditText) as EditText }
    private val lastNameEditText: EditText by lazy { findViewById(R.id.lastNameEditText) as EditText }
    private val saveButton: Button by lazy { findViewById(R.id.savePhoneNumberButton) as Button }

    lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())

        setContentView(R.layout.activity_set_phone_number)

        saveButton.setOnClickListener {
            savePhoneNumber()
        }

        firstNameEditText.afterTextChanged {
            updateButtonEnabledness()
        }

        lastNameEditText.afterTextChanged {
            updateButtonEnabledness()
        }

    }

    private fun updateButtonEnabledness() {
        saveButton.isEnabled = !TextUtils.isEmpty(firstNameEditText.text) && !TextUtils.isEmpty(firstNameEditText.text)
    }

    private fun savePhoneNumber() {
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        if (firstName == null || firstName == null) {
            return
        }
        userRepo.updateName(firstName, lastName, this, {

        }) {
            if (it == null) {
                val user = userRepo.getCurrentUser(this)
                if (user == null) {
                    Log.d("dunno", "something messed up, no user, but signed in")
                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == false) {
                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {

            }
        }
    }

}