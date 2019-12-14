package com.revolut.currencies.base.presentation.view.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

fun ViewGroup.inflate(layout: Int) {
    LayoutInflater.from(context).inflate(layout, this)
}

fun ViewGroup.getInflatedView(layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}

fun LayoutInflater.getInflatedView(layout: Int, container: ViewGroup?): View {
    return inflate(layout, container, false)
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun EditText.onChange(cb: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            cb(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}