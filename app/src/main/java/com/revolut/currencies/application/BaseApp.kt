package com.revolut.currencies.application

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Typeface

@SuppressLint("Registered")
open class BaseApp : Application() {

    private var regular = "fonts/Roboto-Regular.ttf"
    private var medium = "fonts/Roboto-Medium.ttf"

    override fun onCreate() {
        super.onCreate()
        regularFont = Typeface.createFromAsset(assets, regular)
        mediumFont = Typeface.createFromAsset(assets, medium)
    }

    companion object {
        lateinit var regularFont: Typeface
        lateinit var mediumFont: Typeface
    }
}