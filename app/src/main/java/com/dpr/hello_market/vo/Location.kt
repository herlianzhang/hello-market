package com.dpr.hello_market.vo

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Location(
    var address: String? = null,
    var lat: Double? = null,
    var lng: Double? = null
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "address" to address,
            "lat" to lat,
            "lng" to lng
        )
    }
}