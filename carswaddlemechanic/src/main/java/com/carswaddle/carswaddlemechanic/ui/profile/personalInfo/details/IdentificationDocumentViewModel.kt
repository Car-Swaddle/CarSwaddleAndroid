package com.carswaddle.carswaddlemechanic.ui.profile.personalInfo.details

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicRepository
import com.carswaddle.carswaddleandroid.services.IdDocumentImageSide
import com.carswaddle.carswaddleandroid.services.serviceModels.UpdateMechanic
import com.carswaddle.store.AppDatabase
import java.net.URI
import java.net.URL
import java.util.*

class IdentificationDocumentViewModel(application: Application) : AndroidViewModel(application) {

    private val mechanicRepository: MechanicRepository
    
    init {
        val db = AppDatabase.getDatabase(application)
        mechanicRepository = MechanicRepository(db.mechanicDao())
        
    }

    fun uploadIdDocument(filePath: String, side: IdDocumentImageSide, context: Context, completion: (error: Throwable?) -> Unit) {
        mechanicRepository.uploadIdDocument(filePath, side, context, completion)
    }

}