package com.revolut.currencies.ui.rates.domain.interactor

import com.revolut.currencies.base.domain.interactor.SingleUseCase
import com.revolut.currencies.network.response.Response
import com.revolut.currencies.ui.rates.domain.repository.RatesRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(private val repository: RatesRepository) :
    SingleUseCase<String, Response>() {
    override fun build(params: String): Single<Response> =
        repository.getAllRates(baseCurrency = params)
}