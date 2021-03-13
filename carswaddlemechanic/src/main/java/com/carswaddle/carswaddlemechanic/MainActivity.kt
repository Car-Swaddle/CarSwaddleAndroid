package com.carswaddle.carswaddlemechanic

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddlemechanic.pushNotifications.RemoteNotification
import com.carswaddle.store.AppDatabase

class MainActivity : AppCompatActivity() {

    private val userRepo: UserRepository by lazy {
        val userDao = AppDatabase.getDatabase(this).userDao()
        UserRepository(userDao)
    }

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private val bottomNav: BottomNavigationView by lazy { findViewById(R.id.nav_view) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_calendar, R.id.navigation_earnings, R.id.navigation_mechanic_profile)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)
        
        handleRemoteNotificationIfNeeded(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        handleRemoteNotificationIfNeeded(intent)
    }

    private fun handleRemoteNotificationIfNeeded(intent: Intent?) {
        val data = intent?.extras
        if (data == null) { return }
        val r = RemoteNotification.from(data)
        if (r == null) { return }
        handleRemoteNotification(r)
    }

    private fun handleRemoteNotification(r: RemoteNotification) {
        when (r.type) {
            RemoteNotification.Type.userScheduledAutoService -> {
                showAutoService(r.autoServiceId)
            }
            RemoteNotification.Type.reminder -> {
                showAutoService(r.autoServiceId)
            }
            RemoteNotification.Type.mechanicRating -> {
                
            }
            RemoteNotification.Type.autoServiceUpdated -> {
                showAutoService(r.autoServiceId)
            }
            RemoteNotification.Type.userDidRate -> {
                showAutoService(r.autoServiceId)
            }
        }
    }

    private fun showAutoService(autoServiceId: String?) {
        if (autoServiceId == null) {
            return
        }
        bottomNav.selectedItemId = R.id.navigation_calendar
        navController.navigate(
            R.id.action_navigation_calendar_to_autoServiceDetailsFragment,
            null
        )
    }

}
