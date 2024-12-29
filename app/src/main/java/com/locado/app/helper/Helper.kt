package com.locado.app.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.google.android.gms.maps.model.LatLng
import com.locado.app.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Helper {

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusKm * c
    }

    fun distanceInPreferredUnit(userLocation: LatLng, placeLocation: LatLng): String {
        val distanceKm = calculateDistance(
            userLocation.latitude,
            userLocation.longitude,
            placeLocation.latitude,
            placeLocation.longitude
        )
        return if (distanceKm < 1) {
            "${(distanceKm * 1000).toInt()} m"
        } else {
            "%.2f km".format(distanceKm)
        }
    }

    fun showLoadingDialog(context: Context): Dialog {
        val progressDialog = Dialog(context)
        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.progress_dialog)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)

            return it
        }
    }
}