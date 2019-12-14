package com.revolut.currencies.injection.modules

import com.revolut.currencies.BuildConfig
import com.revolut.currencies.network.retrofit.RetrofitClient
import com.revolut.currencies.network.retrofit.RevolutCurrenciesAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule(private val baseURL: String = "") {

    object DAGGER_CONSTANTS {
        const val BASE_URL = "baseUrlString"
    }

    private object ApiEndpointsConstants {
        const val ProductionURL = "https://revolut.duckdns.org/"
        const val StagingURL = "https://revolut.duckdns.org/"
    }

    @Provides
    @Singleton
    @Named(value = DAGGER_CONSTANTS.BASE_URL)
    fun providesBaseUrl() =
        when {
            baseURL.isNotEmpty() -> baseURL
            BuildConfig.QA -> ApiEndpointsConstants.StagingURL
            else -> ApiEndpointsConstants.ProductionURL
        }

    @Provides
    @Singleton
    fun provideHttpClient() = OkHttpClient()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()

    @Provides
    @Singleton
    fun provideRetrofitClient(
        @Named(DAGGER_CONSTANTS.BASE_URL) baseURL: String, httpClient: OkHttpClient,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        builder: Retrofit.Builder
    )
            : Retrofit = RetrofitClient(
        baseURL = baseURL,
        httpClient = httpClient.newBuilder(),
        httpLoggingInterceptor = httpLoggingInterceptor,
        builder = builder
    ).getInstance()

    @Provides
    @Singleton
    fun provideRevolutCurrenciesAPI(retrofit: Retrofit)
            : RevolutCurrenciesAPI = retrofit.create(RevolutCurrenciesAPI::class.java)
}