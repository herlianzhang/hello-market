package com.dpr.hello_market.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.text.TextUtils
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import timber.log.Timber
import java.text.NumberFormat
import java.util.*


object Helper {
    fun isValidEmail(target: String?): Boolean {
        if (target == null) return false
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isValidPassword(target: String?): Boolean {
        if (target == null) return false
        return target.length >= 6
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        return if (phone.trim { it <= ' ' } != "" && phone.length > 10) {
            Patterns.PHONE.matcher(phone).matches()
        } else false
    }

    fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun convertToPriceFormatWithoutCurrency(price: String?): String {
        return try {
            val locale = Locale("in")
            val nf = NumberFormat.getNumberInstance(locale)
            nf.format(price!!.toLong())
        } catch (e: NumberFormatException) {
            Timber.e("Failed Convert currency ${e.printStackTrace()}")
            convertCurrencyManually(price!!)
        }
    }

    private fun convertCurrencyManually(number: String): String {
        val maxIndex = number.length - 1
        var convertedPrice = ""
        var index = 0
        for (i in maxIndex downTo 0) {
            index++

            if (index == 4) {
                index = 0
                convertedPrice = ".$convertedPrice"
            }

            convertedPrice = "${number[i]}$convertedPrice"

        }
        return convertedPrice
    }
}