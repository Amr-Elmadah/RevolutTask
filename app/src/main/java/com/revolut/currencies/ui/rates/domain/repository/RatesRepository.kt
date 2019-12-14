package com.revolut.currencies.ui.rates.domain.repository

import com.revolut.currencies.network.response.Response
import io.reactivex.Single

interface RatesRepository {
    fun getAllRates(baseCurrency: String): Single<Response>
}