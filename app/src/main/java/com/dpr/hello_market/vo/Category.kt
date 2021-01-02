package com.dpr.hello_market.vo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var name: String? = null,
    var picture: String? = null
) : Parcelable