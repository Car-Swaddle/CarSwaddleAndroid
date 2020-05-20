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

//    private val userRepo: UserRepository

//    init {
//        val userDao = AppDatabase.getDatabase(this).userDao()
//        userRepo = UserRepository(userDao)
//    }

    private val userRepo: UserRepository by lazy {
         val userDao = AppDatabase.getDatabase(this).userDao()
         UserRepository(userDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_dashboard
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userRepo.updateCurrentUser(this) {
//            val user = userRepo.getCurrentUser(this)
            val user = userRepo.getDatUser(this)
            Log.d("got user", "user: " + user?.firstName)
        }

    }

}
