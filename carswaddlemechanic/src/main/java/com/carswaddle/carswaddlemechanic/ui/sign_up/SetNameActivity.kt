package com.carswaddle.carswaddlemechanic.ui.sign_up

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.carswaddle.carswaddleandroid.Extensions.afterTextChanged
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.ui.view.ProgressButton
import com.carswaddle.carswaddlemechanic.MainActivity
import com.carswaddle.carswaddlemechanic.R
import com.carswaddle.carswaddlemechanic.ui.login.AuthActivity
import com.carswaddle.services.Authentication
import com.carswaddle.store.AppDatabase


class SetNameActivity : AppCompatActivity() {

    private val firstNameEditText: EditText by lazy { findViewById(R.id.firstNameEditText) as EditText }
    private val lastNameEditText: EditText by lazy { findViewById(R.id.lastNameEditText) as EditText }
    private val saveButton: ProgressButton by lazy { findViewById(R.id.savePhoneNumberButton) as ProgressButton }

    lateinit var userRepo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(application)
        userRepo = UserRepository(db.userDao())

        setContentView(R.layout.activity_set_name)

        saveButton.button.setOnClickListener {
            savePhoneNumber()
        }

        firstNameEditText.afterTextChanged {
            updateButtonEnabledness()
        }

        lastNameEditText.afterTextChanged {
            updateButtonEnabledness()
        }
        
        updateButtonEnabledness()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.profile_action_logout) {
            Log.w("car swaddle android", "logged out")
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

    private fun updateButtonEnabledness() {
        saveButton.isButtonEnabled = !TextUtils.isEmpty(firstNameEditText.text) && !TextUtils.isEmpty(lastNameEditText.text)
    }

    private fun savePhoneNumber() {
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        if (firstName == null || firstName == null) {
            return
        }

        saveButton.isLoading = true

        userRepo.updateName(firstName, lastName, this, {

        }) {
            runOnUiThread {
                saveButton.isLoading = false
            }
            if (it == null) {
                val user = userRepo.getCurrentUser(this)
                if (user == null) {
                    Log.d("dunno", "something messed up, no user, but signed in")
                } else if (user.phoneNumber.isNullOrBlank() || user.isPhoneNumberVerified == false || user.isPhoneNumberVerified == false) {
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