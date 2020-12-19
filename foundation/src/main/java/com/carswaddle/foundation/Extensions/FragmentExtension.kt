package com.carswaddle.carswaddleandroid.Extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


fun Fragment.openSettingsToAppActions() {
    requireActivity().openSettingsToAppActions()
}

fun Fragment.dismissKeyboard() {
    val inputMethodManager: InputMethodManager? =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Activity.openSettingsToAppActions() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:" + packageName)
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}
