package com.carswaddle.carswaddleandroid.ui.activities.autoserviceDetails

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.MacAddress
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.carswaddle.carswaddleandroid.Extensions.openSettingsToAppActions
import com.carswaddle.carswaddleandroid.services.serviceModels.AutoServiceStatus
import com.google.gson.annotations.SerializedName

//class SystemPermission {
//
//
//    fun requestPermission(completion: () -> Unit) {
//
//        if (shouldShowRequestPermissionRationale()) {
//
//        } else {
//
//        }
//
//    }
//
//    fun hasPermission(): Boolean {
//        val c = context ?: return false
//        return ContextCompat.checkSelfPermission(c, permission.mainfestString()) == PackageManager.PERMISSION_GRANTED
//    }
//
//
//
//    enum class Permission {
//        call,
//        sms;
//
//        fun intentString(): String {
//            when (this) {
//                call -> return Intent.ACTION_CALL
//                sms -> return Intent.ACTION_SENDTO
//                else -> return ""
//            }
//        }
//
//        fun mainfestString(): String {
//            when (this) {
//                call -> return Manifest.permission.CALL_PHONE
//                sms -> return Manifest.permission.SEND_SMS
//                else -> return ""
//            }
//        }
//
//        fun askDialogTitle(): String {
//            when(this) {
//                call -> return "Allow call permission to call your mechanic"
//                sms -> return "Allow text permission to text your mechanic"
//            }
//        }
//
//        fun askDialogMessage(): String {
//            when(this) {
//                call -> return "Car Swaddle would like to use the phone to call the mechanic."
//                sms -> return "Allow text permission to text your mechanic"
//            }
//        }
//
//
//        fun permissionDeniedDialogTitle(): String {
//            when(this) {
//                call -> return "Car Swaddle doesn't have permission to call"
//                sms -> return "Car Swaddle doesn't have permission to text"
//            }
//        }
//
//        fun permissionDeniedDialogMessage(): String {
//            when(this) {
//                call -> return "In order for Car Swaddle to make a phone call, you need to allow permission in settings."
//                sms -> return "In order for Car Swaddle to send an sms, you need to allow permission in settings."
//            }
//        }
//
//    }
//
//    private val context: Context
//    private val activity: Activity?
//    private val fragment: Fragment?
//    private val permission: Permission
//
//    constructor(context: Context, activity: Activity, permission: Permission) {
//        this.context = context
//        this.activity = activity
//        this.fragment = null
//        this.permission = permission
//    }
//
//    constructor(context: Context, fragment: Fragment, permission: Permission) {
//        this.context = context
//        this.activity = null
//        this.fragment = fragment
//        this.permission = permission
//    }
//
//    private fun hasAccess() {
//        if (activity != null) {
//            activity.registerActivityLifecycleCallbacks(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//
//            }
////            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
////                if (isGranted) {
////                    Log.w("android", "is granted")
//////                    showCallIntent()
////                } else {
////                    Log.w("android", "is not granted")
////                }
////            }
//        }
//    }
//
//    private fun shouldShowRequestPermissionRationale(): Boolean {
//        if (activity != null) {
//            return activity.shouldShowRequestPermissionRationale(permission.intentString())
//        } else if (fragment != null) {
//            return fragment.shouldShowRequestPermissionRationale(permission.intentString())
//        } else {
//            return false
//        }
//    }
//
//    private fun requestPermission() {
//
////        fragment?.registerForActivityResult()
//
//
////        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
////            if (isGranted) {
////                Log.w("android", "is granted")
////                showCallIntent()
////            } else {
////                Log.w("android", "is not granted")
////            }
////        }
//
//        if (activity != null) {
//
//        } else if (fragment != null) {
//            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//
//            }
//        }
//
//
//        if (shouldShowRequestPermissionRationale()) {
//            showAskCallPermissionDialog()
//        } else {
//            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
//        }
//    }
//
//    private fun showAskPermissionDialog() {
//        val c = context ?: return
//        AlertDialog.Builder(c)
//            .setTitle(permission.askDialogTitle())
//            .setMessage(permission.askDialogMessage())
//            .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    requestCallPermission()
//                }
//            })
//            .setNegativeButton(android.R.string.no, null)
//            .setIcon(android.R.drawable.ic_dialog_alert)
//            .show()
//    }
//
//    private fun showNoPermissionDialog() {
//        val c = context ?: return
//        AlertDialog.Builder(c)
//            .setTitle(permission.permissionDeniedDialogTitle())
//            .setMessage(permission.permissionDeniedDialogMessage())
//            .setPositiveButton(android.R.string.yes, object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    if (activity != null) {
//                        activity.openSettingsToAppActions()
//                    } else if (fragment != null) {
//                        fragment.openSettingsToAppActions()
//                    }
//                }
//            })
//            .setNegativeButton(android.R.string.no, null)
//            .setIcon(android.R.drawable.ic_dialog_alert)
//            .show()
//    }
//
//}