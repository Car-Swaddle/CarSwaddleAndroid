package com.carswaddle.carswaddleandroid.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "image_id") val imageID: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "is_email_verified") val isEmailVerified: Boolean?,
    @ColumnInfo(name = "is_phone_number_verified") val isPhoneNumberVerified: Boolean?,
    @ColumnInfo(name = "time_zone") val timeZone: String?,
    @ColumnInfo(name = "mechanic_id") val mechanicID: String?) {

    constructor(user: com.carswaddle.carswaddleandroid.services.serviceModels.User) :
            this(user.id, user.firstName, user.lastName, user.phoneNumber, user.imageID, user.email, user.isEmailVerified, user.isPhoneNumberVerified, user.timeZone, user.mechanicID)

    fun displayName(): String {
        return firstName + " " + lastName
    }

}
