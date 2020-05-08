package com.carswaddle.carswaddleandroid.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carswaddle.carswaddleandroid.services.serviceModels.Mechanic


@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "image_id") val imageID: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "is_email_verified") val isEmailVerified: String?,
    @ColumnInfo(name = "is_phone_number_verified") val isPhoneNumberVerified: Boolean?,
    @ColumnInfo(name = "time_zone") val timeZone: String?,
    @ColumnInfo(name = "mechanic_id") val mechanicID: String?
)
