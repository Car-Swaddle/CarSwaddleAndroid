package com.carswaddle.carswaddleandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceDao
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationDao
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicDao
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserDao
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleDao
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescription
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescriptionDao
import com.carswaddle.carswaddleandroid.generic.SingletonHolder
import com.carswaddle.carswaddleandroid.services.serviceModels.ServiceEntity

@Database(entities = arrayOf(User::class, AutoService::class, Vehicle::class, AutoServiceLocation::class, VehicleDescription::class, Mechanic::class, ServiceEntity::class, OilChange::class), version = 8)
@TypeConverters(DateConverter::class, CalendarConverter::class, ArrayListConverter::class, AutoServiceStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun autoServiceDao(): AutoServiceDao
    abstract fun mechanicDao(): MechanicDao
    abstract fun locationDao(): AutoServiceLocationDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun vehicleDescriptionDao(): VehicleDescriptionDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "carswaddle"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // TODO - remove this
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}


