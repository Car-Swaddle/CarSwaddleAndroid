package com.carswaddle.carswaddleandroid.messaging

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import java.util.*


data class RemoteNotification(
    val type: Type,
    @SerializedName("autoServiceID") val autoServiceId: String?,
    @SerializedName("userID") val userId: String?,
    @SerializedName("mechanicID") val mechanicId: String?,
    val reviewRating: Float?,
    val mechanicFirstName: String?,
    val date: Date
) {
    
    enum class Type {
        userScheduledAutoService, // The user scheduled a new auto service
        reminder, // Reminder to mechanic or user that an oil change is coming up
        mechanicRating, // Notification to user saying to rate your mechanic
        autoServiceUpdated, // Notification to either mechanic or user that one of their autoservices was updated
        userDidRate // Notification that the user rated a mechanic
    }
    
    companion object {
        fun from(bundle: Bundle): RemoteNotification? {
            val gson = Gson()
            val bundleJson = bundle.toJson()
            val r = gson.fromJson(bundleJson, RemoteNotification::class.java)
            return r
        }
    }
    
}


fun Bundle.toJson(): JsonElement {
    val json = JsonObject()
    val keys = keySet()
    for (key in keys) {
        try {
            val v = this[key]
            if (v is String) {
                json.addProperty(key, v)
            } else if (v is Boolean) {
                json.addProperty(key, v)
            } else if (v is Number) {
                json.addProperty(key, v)
            } else if (v is Char) {
                json.addProperty(key, v)
            }
        } catch (e: JSONException) {
            // Handle exception here
        }
    }
    return json
}