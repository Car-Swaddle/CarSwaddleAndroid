package com.carswaddle.carswaddleandroid.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.carswaddle.carswaddleandroid.data.user.User
import com.carswaddle.carswaddleandroid.data.user.UserDao
import com.carswaddle.carswaddleandroid.generic.SingletonHolder


@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "app.db").build()
    })

}


