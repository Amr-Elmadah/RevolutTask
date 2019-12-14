package com.revolut.currencies.network.retrofit

import com.revolut.currencies.network.response.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutCurrenciesAPI {
    @GET("latest")
    fun loadRates(
        @Query("base") baseCurrency: String
    ): Single<Response>
}