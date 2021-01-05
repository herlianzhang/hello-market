package com.dpr.hello_market.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var name: String? = null,
    var mImage: String? = null
) : Parcelable, Picture(mImage)