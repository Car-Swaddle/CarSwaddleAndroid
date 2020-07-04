package com.carswaddle.carswaddleandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceDao
import com.carswaddle.carswaddleandroid.data.location.Location
import com.carswaddle.carswaddleandroid.data.location.LocationDao
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicDao
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserDao
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleDao
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescription
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescriptionDao
import com.carswaddle.carswaddleandroid.generic.SingletonHolder




@Database(entities = arrayOf(User::class, AutoService::class, Vehicle::class, Location::class, VehicleDescription::class, Mechanic::class), version = 2)
@TypeConverters(DateConverter::class, CalendarConverter::class, ArrayListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun autoServiceDao(): AutoServiceDao
    abstract fun mechanicDao(): MechanicDao
    abstract fun locationDao(): LocationDao
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
                    .allowMainThreadQueries() // TODO - remove this
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}


