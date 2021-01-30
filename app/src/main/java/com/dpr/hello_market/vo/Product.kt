package com.dpr.hello_market.vo

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Product(
    var name: String? = "",
    var price: Long? = 0L,
    var quantity: Long? = 0L,
    var unit: String? = "",
    private val mPicture: String? = ""
) : Parcelable, Picture(mPicture)