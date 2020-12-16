package com.carswaddle.carswaddleandroid.activities.ui

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.user.UserRepository

class MainActivity : AppCompatActivity() {
    
    private val userRepo: UserRepository by lazy {
         val userDao = AppDatabase.getDatabase(this).userDao()
         UserRepository(userDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.bottom_nav_view_nav_host)
        
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_autoservices_list,
            R.id.navigation_profile
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userRepo.importCurrentUser(this) {
            val user = userRepo.getCurrentUser(this)
            Log.d("got user", "user: " + user?.firstName)
        }

    }

}
