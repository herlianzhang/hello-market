package com.dpr.hello_market.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(val mImage: String? = "") : Parcelable, Picture(mImage)