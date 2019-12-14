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

@Parcelize
data class Rates(
    @SerializedName("AUD")
    var AUD: Double,
    @SerializedName("BGN")
    var BGN: Double,
    @SerializedName("BRL")
    var BRL: Double,
    @SerializedName("CAD")
    var CAD: Double,
    @SerializedName("CHF")
    var CHF: Double,
    @SerializedName("CNY")
    var CNY: Double,
    @SerializedName("CZK")
    var CZK: Double,
    @SerializedName("DKK")
    var DKK: Double,
    @SerializedName("EUR")
    var EUR: Double,
    @SerializedName("GBP")
    var GBP: Double,
    @SerializedName("HKD")
    var HKD: Double,
    @SerializedName("HRK")
    var HRK: Double,
    @SerializedName("HUF")
    var HUF: Double,
    @SerializedName("IDR")
    var IDR: Double,
    @SerializedName("ILS")
    var ILS: Double,
    @SerializedName("INR")
    var INR: Double,
    @SerializedName("ISK")
    var ISK: Double,
    @SerializedName("JPY")
    var JPY: Double,
    @SerializedName("KRW")
    var KRW: Double,
    @SerializedName("MXN")
    var MXN: Double,
    @SerializedName("MYR")
    var MYR: Double,
    @SerializedName("NOK")
    var NOK: Double,
    @SerializedName("NZD")
    var NZD: Double,
    @SerializedName("PHP")
    var PHP: Double,
    @SerializedName("PLN")
    var PLN: Double,
    @SerializedName("RON")
    var RON: Double,
    @SerializedName("RUB")
    var RUB: Double,
    @SerializedName("SEK")
    var SEK: Double,
    @SerializedName("SGD")
    var SGD: Double,
    @SerializedName("THB")
    var THB: Double,
    @SerializedName("TRY")
    var TRY: Double,
    @SerializedName("USD")
    var USD: Double,
    @SerializedName("ZAR")
    var ZAR: Double
) : Parcelable