package com.revolut.currencies.base.presentation.view.extension

import java.text.SimpleDateFormat
import java.util.*

fun Date.convertDateToString(
    date: Date? = this,
    format: String = "YYYY-MM-dd hh:mm:ss"
): String {
    return if (date == null) {
        ""
    } else {
        val df = SimpleDateFormat(format, Locale.getDefault())
        " " + df.format(date)
    }
}