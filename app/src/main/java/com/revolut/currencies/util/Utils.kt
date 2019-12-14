package com.revolut.currencies.util

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromAssets(context: Context, fileName: String): T {
    val inputStream: InputStream = context.assets.open(fileName)
    val size: Int = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    val json = String(buffer)
    return this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}

fun Int.dpToPx(): Float {
    return this * Resources.getSystem().displayMetrics.density
}
