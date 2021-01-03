package com.dpr.hello_market.vo

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Product(
    var name: String? = "",
    var picture: String? = "",
    var price: Long? = 0L,
    var quantity: Long? = 0L
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "Picture" to picture,
            "Price" to price,
            "Quantity" to quantity
        )
    }
}