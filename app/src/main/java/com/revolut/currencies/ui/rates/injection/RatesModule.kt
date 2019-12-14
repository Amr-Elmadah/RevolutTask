package com.revolut.currencies.ui.rates.injection

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.revolut.currencies.network.response.Rate
import com.revolut.currencies.network.retrofit.RevolutCurrenciesAPI
import com.revolut.currencies.ui.rates.data.remote.RatesRemoteDataSource
import com.revolut.currencies.ui.rates.domain.interactor.GetRatesUseCase
import com.revolut.currencies.ui.rates.domain.repository.RatesRepository
import com.revolut.currencies.ui.rates.domain.repository.RatesRepositoryImp
import com.revolut.currencies.ui.rates.presentation.view.adapter.RatesAdapter
import com.revolut.currencies.ui.rates.presentation.viewmodel.RatesViewModel
import com.revolut.currencies.util.fromAssets
import dagger.Module
import dagger.Provides

@Module
class RatesModule {

    @Provides
    fun provideRatesRemoteDataSource(RevolutCurrenciesAPI: RevolutCurrenciesAPI) =
        RatesRemoteDataSource(RevolutCurrenciesAPI = RevolutCurrenciesAPI)

    @Provides
    fun provideRatesRepository(
        remoteDataSource: RatesRemoteDataSource
    ): RatesRepository =
        RatesRepositoryImp(remoteDataSource)

    @Provides
    fun provideGetRatesUseCase(repository: RatesRepository) =
        GetRatesUseCase(repository)

    @Provides
    fun provideRatesViewModel(
        getRatesUseCase: GetRatesUseCase
    ) =
        RatesViewModel(getRatesUseCase)

    @Provides
    fun provideLinearLayoutManager(context: Context) =
        LinearLayoutManager(context)

    @Provides
    fun provideRatesAdapter() =
        RatesAdapter()

    @Provides
    fun provideLocalCurrencies(context: Context): ArrayList<Rate> =
        Gson().fromAssets(context = context, fileName = "currencies/currencies.json")
}