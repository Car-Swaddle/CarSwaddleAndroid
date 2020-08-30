package com.carswaddle.carswaddleandroid.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.carswaddle.carswaddleandroid.Extensions.afterTextChanged
import com.carswaddle.carswaddleandroid.R
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
        userRepo.updateName(firstName, lastName, {

        }) {
            val user = userRepo.getCurrentUser(this)
            if (it == null) {
                if (user?.phoneNumber == null || user?.isPhoneNumberVerified == false) {
                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, SetPhoneNumberActivity::class.java)
                    startActivity(intent)
                }
            } else {

            }
        }
    }

}