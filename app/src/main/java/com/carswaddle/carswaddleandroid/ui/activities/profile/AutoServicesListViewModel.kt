package com.carswaddle.carswaddleandroid.activities.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.carswaddleandroid.data.AppDatabase
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AutoServicesListViewModel(application: Application) : AndroidViewModel(application) {

    private val autoServiceRepo: AutoServiceRepository by lazy {
        val autoServiceDao = AppDatabase.getDatabase(application).autoServiceDao()
        AutoServiceRepository(autoServiceDao)
    }

    private val _autoServices = MutableLiveData<AutoService>().apply {
        autoServiceRepo.getAutoServices(100, 0, application, arrayOf<String>()) { error, autoServiceIds ->
            GlobalScope.async {
                if (autoServiceIds != null) {
//                    _autoServices = autoServiceRepo.getAutoServices(autoServiceIds)
                } else {

                }
            }
        }
    }

    val autoServices: LiveData<AutoService> = _autoServices

}


/**
 *
 *
enum Status: String {
case scheduled
case canceled
case inProgress
case completed

public var localizedString: String {
switch self {
case .canceled: return NSLocalizedString("canceled", comment: "auto service status")
case .inProgress: return NSLocalizedString("in progress", comment: "auto service status")
case .completed: return NSLocalizedString("completed", comment: "auto service status")
case .scheduled: return NSLocalizedString("scheduled", comment: "auto service status")
}
}

}
 */

