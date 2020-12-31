package com.dpr.hello_market.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val address: String?,
    val lat: Double?,
    val lng: Double?
) : Parcelable