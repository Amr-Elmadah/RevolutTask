package com.revolut.currencies.network.retrofit

import com.google.gson.GsonBuilder
import com.revolut.currencies.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClient @Inject constructor(
    private val baseURL: String
    , private val httpClient: OkHttpClient.Builder
    , private val httpLoggingInterceptor: HttpLoggingInterceptor
    , private val builder: Retrofit.Builder
) {

    fun getInstance(): Retrofit {
        if (BuildConfig.QA) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(httpLoggingInterceptor)
        }

        val gson = GsonBuilder()
            .setLenient()
            .create()

        builder.baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())

        builder.client(httpClient.build())
        return builder.build()
    }
}



