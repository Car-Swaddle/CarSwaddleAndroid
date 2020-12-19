package com.carswaddle.carswaddleandroid.data.Review

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey val id: String,
    @ColumnInfo val rating: Float,
    @ColumnInfo val text: String,
    @ColumnInfo val reviewerID: String,
    @ColumnInfo val revieweeID: String,
    // Only one of autoServiceIDFromMechanic or autoServiceIDFromUser should exist 
    // Will always be same id, but this determines who created the review, the user (reviewing the mechanic)
    // or the mechanic (reviewing the user)
    @ColumnInfo val autoServiceIDFromUser: String?,
    @ColumnInfo val autoServiceIDFromMechanic: String?
){
    constructor(review: com.carswaddle.carswaddleandroid.services.serviceModels.Review) :
            this(review.id, review.rating, review.text, review.reviewerID, review.revieweeID, review.autoServiceIDFromUser, review.autoServiceIDFromMechanic)

}