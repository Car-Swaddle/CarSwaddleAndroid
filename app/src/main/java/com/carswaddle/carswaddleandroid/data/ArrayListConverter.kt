package com.carswaddle.carswaddleandroid.data

import androidx.room.TypeConverter
import java.util.*

class ArrayListConverter {

    @TypeConverter
    fun toArrayOptional(value: String?): List<String>? {
        return value?.split(",")?.toList()
    }

    @TypeConverter
    fun fromArrayOptional(value: List<String>?): String? {
        return value?.joinToString()
    }

//    @TypeConverter
//    fun toArray(value: String): List<String> {
//        return value.split(",").toList()
//    }
//
//    @TypeConverter
//    fun fromArray(value: List<String>): String {
//        return value.joinToString()
//    }

}