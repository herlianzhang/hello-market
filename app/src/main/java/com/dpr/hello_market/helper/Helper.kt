package com.dpr.hello_market.helper

import android.text.TextUtils
import android.util.Patterns


object Helper {
    fun isValidEmail(target: String?): Boolean {
        if (target == null) return false
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isValidPassword(target: String?): Boolean {
        if (target == null) return false
        return target.length >= 4
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        return if (phone.trim { it <= ' ' } != "" && phone.length > 10) {
            Patterns.PHONE.matcher(phone).matches()
        } else false
    }

}