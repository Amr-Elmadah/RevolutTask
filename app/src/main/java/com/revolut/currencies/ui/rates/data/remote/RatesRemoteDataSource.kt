package com.revolut.currencies.ui.rates.data.remote

import com.revolut.currencies.network.response.Response
import com.revolut.currencies.network.retrofit.RevolutCurrenciesAPI
import io.reactivex.Single
import javax.inject.Inject

class RatesRemoteDataSource @Inject constructor(private val RevolutCurrenciesAPI: RevolutCurrenciesAPI) {

    fun getResponse(baseCurrency: String): Single<Response> =
        RevolutCurrenciesAPI.loadRates(
            baseCurrency
        )
}