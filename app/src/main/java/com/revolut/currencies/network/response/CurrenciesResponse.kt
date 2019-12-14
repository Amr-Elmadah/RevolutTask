package com.revolut.currencies.network.response

import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

data class Response(
    @SerializedName("base")
    var base: String,
    @SerializedName("date")
    var date: Date,
    @SerializedName("rates")
    var ratesObject: JsonObject
) {
    fun getRates(
        baseCurrency: String,
        localCurrencies: ArrayList<Rate>
    ): List<Rate> {
        val currentCurrencies = ArrayList(localCurrencies.map { it.copy() })
        var index = 0
        for (localCurrency in currentCurrencies) {
            if (baseCurrency != localCurrency.currency) {
                localCurrency.value = ratesObject.get(localCurrency.currency).asDouble
            } else {
                index = currentCurrencies.indexOf(localCurrency)
            }
        }
        currentCurrencies.removeAt(index)
        return currentCurrencies
    }
}

@Parcelize
data class Rate(
    @SerializedName("currency")
    var currency: String,
    @SerializedName("currency_name")
    var currencyName: String,
    @SerializedName("image")
    var image: String,
    var value: Double = 0.0
) : Parcelable