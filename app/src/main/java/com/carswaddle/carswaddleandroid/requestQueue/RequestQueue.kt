package com.carswaddle.carswaddleandroid.requestQueue

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley

// Get a RequestQueue
//val queue = ShareRequest.getInstance(this.applicationContext).requestQueue

// Add a request (in this example, called stringRequest) to your RequestQueue.
// ShareRequest.getInstance(this).addToRequestQueue(stringRequest)

//class SharedRequest(context: Context) {
//
//    companion object {
//        @Volatile
//        private var instance: SharedRequest? = null
//        fun getInstance(context: Context) =
//            instance ?: synchronized(this) {
//                instance ?: SharedRequest(context).also {
//                    instance = it
//                }
//            }
//    }
//
//    val imageLoader: ImageLoader by lazy {
//        ImageLoader(requestQueue,
//            object : ImageLoader.ImageCache {
//                private val cache = LruCache<String, Bitmap>(20)
//                override fun getBitmap(url: String): Bitmap {
//                    return cache.get(url)
//                }
//                override fun putBitmap(url: String, bitmap: Bitmap) {
//                    cache.put(url, bitmap)
//                }
//            })
//    }
//
//    val requestQueue: RequestQueue by lazy {
//        // applicationContext is key, it keeps you from leaking the
//        // Activity or BroadcastReceiver if someone passes one in.
//        Volley.newRequestQueue(context.applicationContext)
//    }
//
//    fun <T> addToRequestQueue(req: Request<T>) {
//        requestQueue.add(req)
//    }
//
//}