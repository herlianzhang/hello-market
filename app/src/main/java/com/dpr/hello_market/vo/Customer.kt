package com.dpr.hello_market.vo

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Customer(
    var name: String? = "",
    var email: String? = "",
    var avatar: String? = "",
    var phoneNumber: String? = "",
    var password: String? = "",
    var address: String? = "",
    var lat: Double? = null,
    var lng: Double? = null,
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "avatar" to avatar,
            "phoneNumber" to phoneNumber,
            "password" to password,
            "address" to address,
            "lat" to lat,
            "lng" to lng

        )
    }
}