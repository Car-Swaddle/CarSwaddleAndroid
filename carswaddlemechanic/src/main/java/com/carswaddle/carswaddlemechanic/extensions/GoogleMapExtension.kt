package com.carswaddle.carswaddlemechanic.extensions

import android.content.ContentValues
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import com.carswaddle.carswaddlemechanic.R
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.GoogleMap



fun GoogleMap.updateMapStyle(context: Context) {
    try {
        val mode = context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        var mapStyle = R.raw.standard_map
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> mapStyle = R.raw.night_mode_map
            Configuration.UI_MODE_NIGHT_NO -> {}
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }
        setMapStyle(MapStyleOptions.loadRawResourceStyle(context, mapStyle))
    } catch (e: Resources.NotFoundException) {
        Log.e(ContentValues.TAG, "Can't find style. Error: ", e)
    }
}
