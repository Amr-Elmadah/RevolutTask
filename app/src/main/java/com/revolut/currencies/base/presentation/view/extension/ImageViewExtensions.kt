package com.revolut.currencies.base.presentation.view.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


fun ImageView.loadFromUrl(url: String?, placeholder: Int? = null, isRounded: Boolean = false) {
    val requestOption = RequestOptions()
        .circleCrop()

    val glideApp = Glide.with(context)
        .load(url)
    if (isRounded)
        glideApp.apply(requestOption)

    placeholder?.let {
        glideApp.apply(RequestOptions().placeholder(placeholder))
    }

    glideApp.into(this)
}


