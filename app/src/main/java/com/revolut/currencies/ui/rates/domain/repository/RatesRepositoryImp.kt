package com.revolut.currencies.ui.rates.domain.repository

import com.revolut.currencies.network.response.Response
import com.revolut.currencies.ui.rates.data.remote.RatesRemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class RatesRepositoryImp @Inject constructor(
    private val remoteDataSource: RatesRemoteDataSource
) : RatesRepository {
    override fun getAllRates(baseCurrency: String): Single<Response> =
        remoteDataSource.getResponse(baseCurrency = baseCurrency)
}