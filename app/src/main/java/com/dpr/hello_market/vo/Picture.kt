package com.dpr.hello_market.vo

import android.net.Uri

abstract class Picture(
    var picture: String? = "",
    var imageUri: Uri? = null,
    var isFirst: Boolean = true
)