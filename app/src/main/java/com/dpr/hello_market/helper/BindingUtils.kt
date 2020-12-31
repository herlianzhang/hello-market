package com.dpr.hello_market.helper

import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dpr.hello_market.R


@BindingAdapter("error")
fun setEditTextError(et: EditText, errorMessage: String?) {
    et.error = errorMessage
}

@BindingAdapter("isVisible")
fun isVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("setUriImage")
fun setUriImage(imageView: ImageView, uri: Uri?) {
    uri?.let {
        val context = imageView.context
        Glide.with(context).load(uri)
            .placeholder(CircularProgressDrawable(context).apply {
                centerRadius = 30f
                strokeWidth = 5f
                setColorSchemeColors(R.color.white)
                start()
            }).error(R.drawable.ic_avatar)
            .into(imageView)
    }
}