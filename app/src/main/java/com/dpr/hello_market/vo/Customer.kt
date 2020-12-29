package com.dpr.hello_market.vo

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Customer(
    var name: String? = "",
    var email: String? = "",
    var avatar: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "avatar" to avatar
        )
    }
}