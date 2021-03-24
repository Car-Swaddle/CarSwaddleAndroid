package com.carswaddle.carswaddleandroid.activities.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.data.user.UserRepository
import com.carswaddle.carswaddleandroid.messaging.RemoteNotification
import com.carswaddle.store.AppDatabase

class MainActivity : AppCompatActivity() {

    private val userRepo: UserRepository by lazy {
        val userDao = AppDatabase.getDatabase(this).userDao()
        UserRepository(userDao)
    }

    private val navController: NavController by lazy { findNavController(R.id.bottom_nav_view_nav_host) }
    private val bottomNav: BottomNavigationView by lazy { findViewById(R.id.nav_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_autoservices_list,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNav.setupWithNavController(navController)

        userRepo.importCurrentUser(this) {
            val user = userRepo.getCurrentUser(this)
            Log.d("got user", "user: " + user?.firstName)
        }

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
                // For mechanic, do nothing here
            }
            RemoteNotification.Type.reminder -> {
                // Show auto service
                showAutoService(r.autoServiceId)
            }
            RemoteNotification.Type.mechanicRating -> {
                // Should rate mechanic
                // Go to autoservice and prompt to rate mechanic
                showAutoService(r.autoServiceId, true)
            }
            RemoteNotification.Type.autoServiceUpdated -> {
                // Show auto service
                showAutoService(r.autoServiceId)
            }
            RemoteNotification.Type.userDidRate -> {
                // Do nothing
            }
        }
    }

    private fun showAutoService(autoServiceId: String?, promptRating: Boolean = false) {
        if (autoServiceId == null) {
            return
        }
        bottomNav.selectedItemId = R.id.navigation_autoservices_list
        val bundle = bundleOf("autoServiceId" to autoServiceId, "promptRating" to promptRating)
        navController.navigate(
            R.id.action_navigation_autoservices_list_to_navigation_autoservice_details,
            bundle
        )
    }

}
