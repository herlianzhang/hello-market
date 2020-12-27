package com.dpr.hello_market.helper

import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("error")
fun setEditTextError(et: EditText, errorMessage: String?) {
    et.error = errorMessage
}

@BindingAdapter("isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}