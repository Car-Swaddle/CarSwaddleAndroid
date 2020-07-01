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

    init {
        loadAutoServices()
    }

    private val autoServiceRepo: AutoServiceRepository by lazy {
        val autoServiceDao = AppDatabase.getDatabase(application).autoServiceDao()
        AutoServiceRepository(autoServiceDao)
    }

    private val _autoServices = MutableLiveData<List<AutoService>>()

    private fun loadAutoServices() {
        autoServiceRepo.getAutoServices(100, 0, getApplication(), listOf<String>()) { error, autoServiceIds ->
            GlobalScope.async {
                if (autoServiceIds != null) {
                    _autoServices.value = autoServiceRepo.getAutoServices(autoServiceIds)
                } else {
                    // TODO: do something!
                }
            }
        }
    }

    val autoServices: LiveData<List<AutoService>>
        get() = _autoServices

//    fun getAutoServices(): LiveData<List<AutoService>> {
//        return _autoServices
//    }

//    var autoServices: LiveData<Array<AutoService>> = LiveData<Array<AutoService>>()

//    var autoServices: LiveData<Array<com.carswaddle.carswaddleandroid.data.autoservice.AutoService>>

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

