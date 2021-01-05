package com.dpr.hello_market.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.net.Uri
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dpr.hello_market.BuildConfig
import com.dpr.hello_market.R
import com.dpr.hello_market.vo.Picture
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    private suspend fun downloadImage(picture: String): Uri? {
        val storageRef = Firebase.storage
        return try {
            withContext(Dispatchers.IO) {
                storageRef.getReferenceFromUrl(BuildConfig.STORAGE_URL)
                    .child(picture).downloadUrl.await()
            }
        } catch (e: Exception) {
            null
        }
    }

    fun setImage(item: Picture, imageView: ImageView) {
        if (item.imageUri == null) {
            Glide.with(imageView.context).load(R.color.color_grey).into(imageView)
            if (item.isFirst) {
                item.isFirst = false
                CoroutineScope(Dispatchers.Main).launch {
                    val imageUri = downloadImage(item.picture ?: "")
                    item.imageUri = imageUri
                    Glide.with(imageView.context).load(imageUri).into(imageView)
                }
            }
        } else {
            Glide.with(imageView.context).load(item.imageUri).into(imageView)
        }
    }
}