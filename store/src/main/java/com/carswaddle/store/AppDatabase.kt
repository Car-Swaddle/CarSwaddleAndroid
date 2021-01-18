package com.carswaddle.store

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carswaddle.carswaddleandroid.data.Review.ReviewDao
import com.carswaddle.carswaddleandroid.data.autoservice.AutoService
import com.carswaddle.carswaddleandroid.data.autoservice.AutoServiceDao
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocation
import com.carswaddle.carswaddleandroid.data.location.AutoServiceLocationDao
import com.carswaddle.carswaddleandroid.data.mechanic.Mechanic
import com.carswaddle.carswaddleandroid.data.mechanic.MechanicDao
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpan
import com.carswaddle.carswaddleandroid.data.mechanic.TemplateTimeSpanDao
import com.carswaddle.carswaddleandroid.data.oilChange.OilChange
import com.carswaddle.carswaddleandroid.data.oilChange.OilChangeDao
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserDao
import com.carswaddle.carswaddleandroid.data.vehicle.Vehicle
import com.carswaddle.carswaddleandroid.data.vehicle.VehicleDao
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescription
import com.carswaddle.carswaddleandroid.data.vehicleDescription.VehicleDescriptionDao
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntity
import com.carswaddle.carswaddleandroid.data.serviceEntity.ServiceEntityDao
import com.carswaddle.carswaddleandroid.data.Review.Review
import com.carswaddle.store.balance.Balance
import com.carswaddle.store.balance.BalanceDao
import com.carswaddle.store.payout.Payout
import com.carswaddle.store.payout.PayoutDao
import com.carswaddle.store.transaction.Transaction
import com.carswaddle.store.transaction.TransactionDao
import com.carswaddle.store.transaction.TransactionMetadata


@Database(entities = arrayOf(User::class, AutoService::class, Vehicle::class, AutoServiceLocation::class, VehicleDescription::class, Mechanic::class, ServiceEntity::class, OilChange::class, TemplateTimeSpan::class, Review::class, Balance::class, Payout::class, Transaction::class, TransactionMetadata::class), version = 18)
@TypeConverters(DateConverter::class, DateDoubleConverter::class, DateIntConverter::class, CalendarConverter::class, CalendarDoubleConverter::class, CalendarIntConverter::class, ArrayListConverter::class, AutoServiceStatusConverter::class, OilTypeConverter::class, PayoutStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun autoServiceDao(): AutoServiceDao
    abstract fun mechanicDao(): MechanicDao
    abstract fun locationDao(): AutoServiceLocationDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun vehicleDescriptionDao(): VehicleDescriptionDao
    abstract fun serviceEntityDao(): ServiceEntityDao
    abstract fun oilChangeDao(): OilChangeDao
    abstract fun templateTimeSpanDao(): TemplateTimeSpanDao
    abstract fun reviewDao(): ReviewDao
    abstract fun balanceDao(): BalanceDao
    abstract fun transactionDao(): TransactionDao
    abstract fun payoutDao(): PayoutDao

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


