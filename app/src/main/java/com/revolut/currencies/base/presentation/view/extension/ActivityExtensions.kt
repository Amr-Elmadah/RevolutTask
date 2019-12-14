package com.revolut.currencies.base.presentation.view.extension

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.onBackPresseddd() {
    if (supportFragmentManager.backStackEntryCount > 1) supportFragmentManager.popBackStack()
    else finish()
}

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view = currentFocus
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AppCompatActivity.hideKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun Activity.getWindowWidth(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Activity.getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}