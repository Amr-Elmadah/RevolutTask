package com.revolut.currencies.base.presentation.view.extension

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar


inline fun View.showSnack(
    @StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG,
    doAction: Snackbar.() -> Unit
) {
    showSnack(resources.getString(messageRes), length, doAction)
}

inline fun View.showSnack(
    message: String, length: Int = Snackbar.LENGTH_LONG,
    doAction: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.doAction()
    snack.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    snack.show()
}

fun View.hideSnack(snack: Snackbar) {
    snack.dismiss()
}

fun View.showSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    val mainTextView =
        snack.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    mainTextView.setTextColor(Color.WHITE)
    mainTextView.gravity = Gravity.CENTER_HORIZONTAL
    snack.view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}


fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}