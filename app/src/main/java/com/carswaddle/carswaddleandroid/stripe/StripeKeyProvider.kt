package com.carswaddle.carswaddleandroid.stripe

import android.content.Context
import androidx.annotation.Size
import com.carswaddle.carswaddleandroid.retrofit.ServiceGenerator
import com.carswaddle.carswaddleandroid.services.StripeService
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StripeKeyProvider(val context: Context) : EphemeralKeyProvider {

    override fun createEphemeralKey(
        @Size(min = 4) apiVersion: String,
        keyUpdateListener: EphemeralKeyUpdateListener
    ) {
        val service = ServiceGenerator.authenticated(context)?.retrofit?.create(StripeService::class.java)
        if (service == null) {
            keyUpdateListener.onKeyUpdateFailure(0, "Could not create service")
            return
        }
        
        val call = service.getEphemeralKeys(apiVersion)
        call.enqueue(object : Callback<Map<String, Any>> {

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                keyUpdateListener.onKeyUpdateFailure(0, "somethind gone done gone wrong")
            }

            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                val map = response.body()
                if (map == null) {
                    keyUpdateListener.onKeyUpdateFailure(0, "Invalid body in response")
                    return
                }
                try {
                    val stringJSON = JSONObject(map).toString()
                    keyUpdateListener.onKeyUpdate(stringJSON)
                } catch(e: JSONException) {
                    keyUpdateListener.onKeyUpdateFailure(0, "Etwa ist incorrekt")
                }
            }
        })
        
    }
}